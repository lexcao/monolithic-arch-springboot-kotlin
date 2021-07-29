package io.lexcao.bookstore.domain.payment

import io.lexcao.bookstore.application.payment.dto.Settlement
import io.lexcao.bookstore.infrastructure.cache.CacheConfiguration
import mu.KLogging
import org.springframework.cache.Cache
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.Timer
import javax.annotation.Resource
import javax.persistence.EntityNotFoundException
import kotlin.concurrent.timerTask

/**
 * 支付单相关的领域服务
 */
@Service
class PaymentService(
    val stockpileService: StockpileService,
    val paymentRepository: PaymentRepository,
    @Resource(name = "settlement") val settlementCache: Cache
) {

    private val timer = Timer()

    companion object : KLogging() {
        /**
         * 默认支付单超时时间：2分钟（缓存TTL时间的一半）
         */
        private const val DEFAULT_PRODUCT_FROZEN_EXPIRES = CacheConfiguration.SYSTEM_DEFAULT_EXPIRES / 2
    }

    /**
     * 生成支付单
     *
     * 根据结算单冻结指定的货物，计算总价，生成支付单
     */
    fun producePayment(bill: Settlement): Payment {
        val total = bill.items.sumOf {
            stockpileService.frozen(it.productId, it.amount)
            bill.productMap[it.productId]?.price ?: 0.0 * it.amount
        } + 12 // 12元固定运费，客户端写死的，这里陪着演一下，避免总价对不上
        val payment = Payment(total, DEFAULT_PRODUCT_FROZEN_EXPIRES)
        paymentRepository.save(payment)
        // 将支付单存入缓存
        settlementCache.put(payment.payId, bill)
        logger.info("创建支付订单，总额：{}", payment.totalPrice)
        return payment
    }

    /**
     * 完成支付单
     *
     * 意味着客户已经完成付款，这个方法在正式业务中应当作为三方支付平台的回调，而演示项目就直接由客户端发起调用了
     */
    fun accomplish(payId: String): Double {
        synchronized(payId.intern()) {
            val payment = paymentRepository.getByPayId(payId)
                ?: throw EntityNotFoundException(payId)
            if (payment.payState != Payment.State.WAITING) {
                throw UnsupportedOperationException("当前订单不允许支付，当前状态为：" + payment.payState)
            }

            payment.payState = Payment.State.PAYED
            paymentRepository.save(payment)
            accomplishSettlement(Payment.State.PAYED, payment.payId)
            logger.info("编号为{}的支付单已处理完成，等待支付", payId)
            return payment.totalPrice
        }
    }

    /**
     * 取消支付单
     *
     *
     * 客户取消支付单，此时应当立即释放处于冻结状态的库存
     * 由于支付单的存储中应该保存而未持久化的购物明细（在Settlement中），所以这步就不做处理了，等2分钟后在触发器中释放
     */
    fun cancel(payId: String) {
        synchronized(payId.intern()) {
            val payment = paymentRepository.getByPayId(payId)
                ?: throw EntityNotFoundException(payId)
            if (payment.payState != Payment.State.WAITING) {
                throw UnsupportedOperationException("当前订单不允许取消，当前状态为：" + payment.payState)
            }

            payment.payState = Payment.State.CANCEL
            paymentRepository.save(payment)
            accomplishSettlement(Payment.State.CANCEL, payment.payId)
            logger.info("编号为{}的支付单已被取消", payId)
        }
    }

    /**
     * 设置支付单自动冲销解冻的触发器
     *
     *
     * 如果在触发器超时之后，如果支付单未仍未被支付（状态是WAITING）
     * 则自动执行冲销，将冻结的库存商品解冻，以便其他人可以购买，并将Payment的状态修改为ROLLBACK。
     *
     *
     * 注意：
     * 使用TimerTask意味着节点带有状态，这在分布式应用中是必须明确【反对】的，如以下缺陷：
     * 1. 如果要考虑支付订单的取消场景，无论支付状态如何，这个TimerTask到时间之后都应当被执行。不应尝试使用TimerTask::cancel来取消任务。
     * 因为只有带有上下文状态的节点才能完成取消操作，如果要在集群中这样做，就必须使用支持集群的定时任务（如Quartz）以保证多节点下能够正常取消任务。
     * 2. 如果节点被重启、同样会面临到状态的丢失，导致一部分处于冻结的触发器永远无法被执行，所以需要系统启动时根据数据库状态有一个恢复TimeTask的的操作
     * 3. 即时只考虑正常支付的情况，真正生产环境中这种代码需要一个支持集群的同步锁（如用Redis实现互斥量），避免解冻支付和该支付单被完成两个事件同时在不同的节点中发生
     */
    fun setupAutoThawedTrigger(payment: Payment) {
        timer.schedule(timerTask {
            synchronized(payment.payId.intern()) {
                // 使用2分钟之前的Payment到数据库中查出当前的Payment
                val currentPayment = paymentRepository.findByIdOrNull(payment.id)
                    ?: throw EntityNotFoundException(payment.id.toString())

                if (currentPayment.payState == Payment.State.WAITING) {
                    logger.info("支付单{}当前状态为：WAITING，转变为：TIMEOUT", payment.id)
                    accomplishSettlement(Payment.State.TIMEOUT, payment.payId)
                }
            }
        }, payment.expires)
    }

    /**
     * 根据支付状态，实际调整库存（扣减库存或者解冻）
     */
    private fun accomplishSettlement(endState: Payment.State, payId: String) {
        settlementCache.get(payId, Settlement::class.java)?.items?.forEach {
            if (endState == Payment.State.PAYED) {
                stockpileService.decrease(it.productId, it.amount)
            } else {
                // 其他状态，无论是TIMEOUT还是CANCEL，都进行解冻
                stockpileService.thawed(it.productId, it.amount)
            }
        }
    }
}

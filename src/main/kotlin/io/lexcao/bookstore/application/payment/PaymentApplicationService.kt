package io.lexcao.bookstore.application.payment

import io.lexcao.bookstore.application.payment.dto.Settlement
import io.lexcao.bookstore.domain.payment.Payment
import io.lexcao.bookstore.domain.payment.PaymentService
import io.lexcao.bookstore.domain.payment.WalletService
import io.lexcao.bookstore.domain.warehouse.ProductService
import org.springframework.cache.Cache
import org.springframework.stereotype.Service
import javax.annotation.Resource
import javax.transaction.Transactional

/**
 * 支付应用务
 */
@Service
@Transactional
class PaymentApplicationService(
    val walletService: WalletService,
    val paymentService: PaymentService,
    val productService: ProductService,
    @Resource(name = "settlement") val settlementCache: Cache
) {

    /**
     * 根据结算清单的内容执行，生成对应的支付单
     */
    fun executeBySettlement(bill: Settlement): Payment {
        // 从服务中获取商品的价格，计算要支付的总价（安全原因，这个不能由客户端传上来）
        productService.replenishProductInformation(bill)
        // 冻结部分库存（保证有货提供）,生成付款单
        val payment = paymentService.producePayment(bill)
        // 设立解冻定时器（超时未支付则释放冻结的库存和资金）
        paymentService.setupAutoThawedTrigger(payment)
        return payment
    }

    /**
     * 完成支付
     * 立即取消解冻定时器，执行扣减库存和资金
     */
    fun accomplishPayment(accountId: Int, payId: String) {
        // 订单从冻结状态变为派送状态，扣减库存
        val price = paymentService.accomplish(payId)
        // 扣减货款
        walletService.decrease(accountId, price)
        // 支付成功的清除缓存
        settlementCache.evict(payId)
    }

    /**
     * 取消支付
     * 立即触发解冻定时器，释放库存和资金
     */
    fun cancelPayment(payId: String) {
        // 释放冻结的库存
        paymentService.cancel(payId)
        // 取消支付的清除缓存
        settlementCache.evict(payId)
    }
}

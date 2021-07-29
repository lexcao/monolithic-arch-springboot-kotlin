package io.lexcao.bookstore.domain.payment

import io.lexcao.bookstore.domain.BaseEntity
import io.lexcao.bookstore.domain.auth.AuthenticAccount
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Date
import java.util.UUID
import javax.persistence.Entity

/**
 * 支付单模型
 *
 * 就是传到客户端让用户给扫码或者其他别的方式付钱的对象
 */
@Entity
data class Payment(
    val totalPrice: Double = 0.0,
    val expires: Long = 0,
    // 下面这两个是随便写的，实际应该根据情况调用支付服务，返回待支付的ID
    val payId: String = UUID.randomUUID().toString()
) : BaseEntity() {
    // 产生支付单的时候一定是有用户的
    val paymentLink: String = "/pay/modify/" + payId + "?state=PAYED&accountId=" +
        (SecurityContextHolder.getContext()?.authentication?.principal as? AuthenticAccount)?.id

    var payState: State = State.WAITING
    val createTime: Date = Date()

    /**
     * 支付状态
     */
    enum class State {
        /**
         * 等待支付中
         */
        WAITING,

        /**
         * 已取消
         */
        CANCEL,

        /**
         * 已支付
         */
        PAYED,

        /**
         * 已超时回滚（未支付，并且商品已恢复）
         */
        TIMEOUT
    }
}

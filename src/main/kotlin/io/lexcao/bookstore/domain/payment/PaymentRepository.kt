package io.lexcao.bookstore.domain.payment

import org.springframework.data.repository.CrudRepository

/**
 * 支付单数据仓库
 */
interface PaymentRepository : CrudRepository<Payment, Int> {
    fun getByPayId(payId: String): Payment?
}

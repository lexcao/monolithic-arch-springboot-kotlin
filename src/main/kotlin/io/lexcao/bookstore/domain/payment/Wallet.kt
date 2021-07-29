package io.lexcao.bookstore.domain.payment

import io.lexcao.bookstore.domain.BaseEntity
import io.lexcao.bookstore.domain.account.Account
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

/**
 * 用户钱包
 */
@Entity
data class Wallet(
    // 这里是偷懒，正式项目中请使用BigDecimal来表示金额
    var money: Double = 0.0
) : BaseEntity() {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    lateinit var account: Account
}

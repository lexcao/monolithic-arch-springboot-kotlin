package io.lexcao.bookstore.domain.payment

import org.springframework.data.repository.CrudRepository

/**
 * 钱包数据仓库
 */
interface WalletRepository : CrudRepository<Wallet, Int> {
    fun findByAccountId(accountId: Int): Wallet?
}

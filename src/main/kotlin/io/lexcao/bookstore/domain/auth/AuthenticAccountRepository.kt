package io.lexcao.bookstore.domain.auth

import io.lexcao.bookstore.domain.account.AccountRepository
import org.springframework.stereotype.Component

/**
 * 认证用户的数据仓库
 */
@Component
class AuthenticAccountRepository(
    private val databaseUserRepo: AccountRepository
) {

    fun findByUsername(username: String): AuthenticAccount? = databaseUserRepo.findByUsername(username)
        ?.let { AuthenticAccount(it) }
}

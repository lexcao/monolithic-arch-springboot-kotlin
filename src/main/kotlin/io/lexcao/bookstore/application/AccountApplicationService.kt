package io.lexcao.bookstore.application

import io.lexcao.bookstore.domain.account.Account
import io.lexcao.bookstore.domain.account.AccountRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 用户资源的应用服务接口
 */
@Service
@Transactional
class AccountApplicationService(
    val repository: AccountRepository,
    val passwordEncoder: PasswordEncoder
) {

    fun createAccount(account: Account) {
        account.password = passwordEncoder.encode(account.password)
        repository.save(account)
    }

    fun findAccountByUsername(username: String): Account? {
        return repository.findByUsername(username)
    }

    fun updateAccount(account: Account) {
        repository.save(account)
    }
}

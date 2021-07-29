package io.lexcao.bookstore.domain.auth.service

import io.lexcao.bookstore.domain.auth.AuthenticAccountRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import javax.inject.Named

/**
 * 认证用户信息查询服务
 *
 *
 * [UserDetailsService]接口定义了从外部（数据库、LDAP，任何地方）根据用户名查询到
 */
@Named
class AuthenticAccountDetailsService(
    private val accountRepository: AuthenticAccountRepository
) : UserDetailsService {

    /**
     * 根据用户名查询用户角色、权限等信息
     * 如果用户名无法查询到对应的用户，或者权限不满足，请直接抛出[UsernameNotFoundException]，勿返回null
     */
    override fun loadUserByUsername(username: String): UserDetails {
        return accountRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("用户 $username 不存在")
    }
}

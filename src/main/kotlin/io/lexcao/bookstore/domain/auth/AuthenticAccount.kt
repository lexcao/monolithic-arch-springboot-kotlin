package io.lexcao.bookstore.domain.auth

import io.lexcao.bookstore.domain.account.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 认证用户模型
 *
 *
 * 用户注册之后，包含其业务属性，如姓名、电话、地址，用于业务发生，存储于Account对象中
 * 也包含其用于认证的属性，譬如密码、角色、是否停用，存储于AuthenticAccount对象中
 */
class AuthenticAccount(private val account: Account) : UserDetails {

    val id: Int get() = account.id

    /**
     * 该用户拥有的授权，譬如读取权限、修改权限、增加权限等等
     */
    private val authorities: MutableCollection<GrantedAuthority> = HashSet()

    init {
        // 由于没有做用户管理功能，默认给系统中第一个用户赋予管理员角色
        if (account.id == 1) {
            authorities.add(SimpleGrantedAuthority(Role.ADMIN))
        }
        authorities.add(SimpleGrantedAuthority(Role.USER))
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String = account.password
    override fun getUsername(): String = account.username

    /**
     * 账号是否过期
     */
    override fun isAccountNonExpired(): Boolean = true

    /**
     * 是否锁定
     */
    override fun isAccountNonLocked(): Boolean = true

    /**
     * 密码是否过期
     */
    override fun isCredentialsNonExpired(): Boolean = true

    /**
     * 是否被锁定
     */
    override fun isEnabled(): Boolean = true
}

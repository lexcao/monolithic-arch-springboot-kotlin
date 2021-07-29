package io.lexcao.bookstore.domain.auth.provider

import io.lexcao.bookstore.domain.auth.service.AuthenticAccountDetailsService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import javax.inject.Named

/**
 * 基于用户名、密码的身份认证器
 * 该身份认证器会被[AuthenticationManager]验证管理器调用
 * 验证管理器支持多种验证方式，这里基于用户名、密码的的身份认证是方式之一
 */
@Named
class UsernamePasswordAuthenticationProvider(
    private val passwordEncoder: PasswordEncoder,
    private val authenticAccountDetailsService: AuthenticAccountDetailsService
) : AuthenticationProvider {

    /**
     * 认证处理
     *
     *
     * 根据用户名查询用户资料，对比资料中加密后的密码
     * 结果将返回一个Authentication的实现类（此处为UsernamePasswordAuthenticationToken）则代表认证成功
     * 返回null或者抛出AuthenticationException的子类异常则代表认证失败
     */
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name.lowercase()
        val password = authentication.credentials as String
        // AuthenticationException的子类定义了多种认证失败的类型，这里仅处理"用户不存在"、“密码不正确”两种
        // 用户不存在的话会直接由loadUserByUsername()抛出异常
        val user = authenticAccountDetailsService.loadUserByUsername(username)
        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("密码不正确")
        }
        // 认证通过，返回令牌
        return UsernamePasswordAuthenticationToken(user, password, user.authorities)
    }

    /**
     * 判断该验证器能处理哪些认证
     */
    override fun supports(clazz: Class<*>): Boolean {
        return clazz == UsernamePasswordAuthenticationToken::class.java
    }
}

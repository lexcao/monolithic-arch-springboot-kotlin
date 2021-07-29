package io.lexcao.bookstore.infrastructure.configuration

import io.lexcao.bookstore.domain.auth.provider.PreAuthenticatedAuthenticationProvider
import io.lexcao.bookstore.domain.auth.provider.UsernamePasswordAuthenticationProvider
import io.lexcao.bookstore.domain.auth.service.AuthenticAccountDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Spring Security的用户认证服务器配置
 *
 *
 * 借用Spring Security作为认证服务器，告知服务器通过怎样的途径去查询用户、加密密码和验证用户真伪
 * 我们实际上并不使用Spring Security提供的认证表单，而是选择了前端通过OAuth2的密码模式，在授权过程中同时完成认证
 * 由于服务端整套安全机制（方法授权判断、OAuth2密码模式的用户认证、密码的加密算法）仍然是构建在Spring Security基础之上
 * 所以我们的认证服务、用户信息服务仍然继承着Spring Security提供的基类，并在这里注册到Spring Security当中
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
class AuthenticationServerConfiguration(
    val encoder: PasswordEncoder,
    val preProvider: PreAuthenticatedAuthenticationProvider,
    val userProvider: UsernamePasswordAuthenticationProvider,
    val authenticAccountDetailsService: AuthenticAccountDetailsService,
) : WebSecurityConfiguration() {

    /**
     * 需要把AuthenticationManager主动暴漏出来
     * 以便在授权服务器[AuthorizationServerConfiguration]中可以使用它来完成用户名、密码的认证
     */
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    /**
     * 配置Spring Security的安全认证服务
     * Spring Security的Web安全设置，将在资源服务器配置[ResourceServerConfiguration]中完成
     */
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(authenticAccountDetailsService).passwordEncoder(encoder)
        auth.authenticationProvider(userProvider)
        auth.authenticationProvider(preProvider)
    }
}

package io.lexcao.bookstore.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Spring Security安全配置
 * <p>
 * 移除静态资源目录的安全控制，避免Spring Security默认禁止HTTP缓存的行为
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.headers().cacheControl().disable()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/static/**")
    }

    /**
     * 配置认证使用的密码加密算法：BCrypt
     * 由于在Spring Security很多验证器中都要用到[PasswordEncoder]的加密，所以这里要添加@Bean注解发布出去
     */
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

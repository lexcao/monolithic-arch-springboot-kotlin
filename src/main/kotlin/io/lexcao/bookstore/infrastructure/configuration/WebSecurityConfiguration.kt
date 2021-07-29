package io.lexcao.bookstore.infrastructure.configuration

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * Spring Security安全配置
 * <p>
 * 移除静态资源目录的安全控制，避免Spring Security默认禁止HTTP缓存的行为
 **/
abstract class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.headers().cacheControl().disable()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/static/**")
    }
}

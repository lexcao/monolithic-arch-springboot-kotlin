package io.lexcao.bookstore.infrastructure.utils

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.inject.Named

@Named
class Encrypt {

    /**
     * 配置认证使用的密码加密算法：BCrypt
     * 由于在Spring Security很多验证器中都要用到[PasswordEncoder]的加密，所以这里要添加@Bean注解发布出去
     */
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

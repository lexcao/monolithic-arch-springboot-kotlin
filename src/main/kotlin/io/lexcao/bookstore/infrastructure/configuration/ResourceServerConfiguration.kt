package io.lexcao.bookstore.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

/**
 * 资源服务器配置
 *
 *
 * 配置资源服务访问权限，主流有两种方式：
 * 一是在这里通过[HttpSecurity]的`antMatchers`方法集中配置
 * 二是启用全局方法级安全支持[EnableGlobalMethodSecurity] 在各个资源的访问方法前，通过注解来逐个配置，使用的注解包括有：
 * JSR 250标准注解[RolesAllowed]，可完整替代Spring的[Secured]功能
 * 以及可以使用EL表达式的Spring注解[PreAuthorize]、[PostAuthorize]
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/7 19:43
 */
@Configuration
@EnableResourceServer
class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {

    /**
     * 配置HTTP访问相关的安全选项
     */
    override fun configure(http: HttpSecurity) {
        // 基于JWT来绑定用户状态，所以服务端可以是无状态的
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // 关闭CSRF（Cross Site Request Forgery）跨站请求伪造的防御
        // 因为需要状态存储CSRF Token才能开启该功能
        http.csrf().disable()
        // 关闭HTTP Header中的X-Frame-Options选项，允许页面在frame标签中打开
        http.headers().frameOptions().disable()
        // 设置服务的安全规则
        http.authorizeRequests().anyRequest().permitAll() // antMatchers("/oauth/**").permitAll()
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
//        resources.tokenServices(tokenService)
    }
}

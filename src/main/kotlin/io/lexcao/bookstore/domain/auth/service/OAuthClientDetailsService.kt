package io.lexcao.bookstore.domain.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import javax.inject.Named

/**
 * OAuth2客户端类型定义
 *
 *
 * OAuth2支持四种授权模式，这里仅定义了密码模式（Resource Owner Password Credentials Grant）一种
 * OAuth2作为开放的（面向不同服务提供商）授权协议，要求用户提供明文用户名、密码的这种“密码模式”并不常用
 * 而这里可以采用是因为前端（BookStore FrontEnd）与后端服务是属于同一个服务提供者的，实质上不存在密码会不会被第三方保存的敏感问题
 * 如果永远只考虑单体架构、单一服务提供者，则并无引入OAuth的必要，Spring Security的表单认证就能很良好、便捷地解决认证和授权的问题
 * 这里使用密码模式来解决，是为了下一阶段演示微服务化后，服务之间鉴权作准备，以便后续扩展以及对比。
 */
@Named
class OAuthClientDetailsService(
    passwordEncoder: PasswordEncoder,
) : ClientDetailsService {

    /**
     * 构造密码授权模式
     *
     *
     * 由于实质上只有一个客户端，所以就不考虑存储和客户端的增删改查了，直接在内存中配置出客户端的信息
     *
     *
     * 授权Endpoint示例：
     * /oauth/token?grant_type=password & username=#USER# & password=#PWD# & client_id=bookstore_frontend & client_secret=bookstore_secret
     * 刷新令牌Endpoint示例：
     * /oauth/token?grant_type=refresh_token & refresh_token=#REFRESH_TOKEN# & client_id=bookstore_frontend & client_secret=bookstore_secret
     */
    private val clientDetailsService: ClientDetailsService = InMemoryClientDetailsServiceBuilder()
        .apply {
            withClient(CLIENT_ID)
                .secret(passwordEncoder.encode(CLIENT_SECRET))
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("BROWSER")
        }.build()


    /**
     * 外部根据客户端ID查询验证方式
     */
    override fun loadClientByClientId(clientId: String): ClientDetails {
        return clientDetailsService.loadClientByClientId(clientId)
    }

    companion object {
        /**
         * 客户端ID
         * 这里的客户端就是指本项目的前端代码
         */
        private const val CLIENT_ID = "bookstore_frontend"

        /**
         * 客户端密钥
         * 在OAuth2协议中，ID是可以公开的，密钥应当保密，密钥用以证明当前申请授权的客户端是未被冒充的
         */
        private const val CLIENT_SECRET = "bookstore_secret"
    }
}

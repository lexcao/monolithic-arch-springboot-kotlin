package io.lexcao.bookstore.domain.auth.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.stereotype.Service

/**
 * JWT访问令牌服务
 *
 *
 * 在此服务中提供了令牌如何存储、携带哪些信息、如何签名、持续多长时间等相关内容的定义
 * 令牌服务应当会被授权服务器[io.lexcao.bookstore.infrastructure.configuration.AuthorizationServerConfiguration]注册验证Endpoint时候调用到
 */
@Service
class JWTAccessTokenService (
    token: JWTAccessToken,
    clientService: OAuthClientDetailsService,
    authenticationManager: AuthenticationManager
) : DefaultTokenServices() {
    /**
     * 构建JWT令牌，并进行默认的配置
     */
    init {
        // 设置令牌的持久化容器
        // 令牌持久化有多种方式，单节点服务可以存放在Session中，集群可以存放在Redis中
        // 而JWT是后端无状态、前端存储的解决方案，Token的存储由前端完成
        setTokenStore(JwtTokenStore(token))
        // 令牌支持的客户端详情
        setClientDetailsService(clientService)
        // 设置验证管理器，在鉴权的时候需要用到
        setAuthenticationManager(authenticationManager)
        // 定义令牌的额外负载
        setTokenEnhancer(token)
        // access_token有效期，单位：秒，默认12小时
        setAccessTokenValiditySeconds(60 * 60 * 3)
        // refresh_token的有效期，单位：秒, 默认30天
        // 这决定了客户端选择“记住当前登录用户”的最长时效，即失效前都不用再请求用户赋权了
        setRefreshTokenValiditySeconds(60 * 60 * 24 * 15)
        // 是否支持refresh_token，默认false
        setSupportRefreshToken(true)
        // 是否复用refresh_token，默认为true
        // 如果为false，则每次请求刷新都会删除旧的refresh_token，创建新的refresh_token
        setReuseRefreshToken(true)
    }
}

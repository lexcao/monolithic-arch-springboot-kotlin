package io.lexcao.bookstore.infrastructure.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.fenixsoft.bookstore.infrastructure.cache.CacheConfiguration
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

/**
 * 为系统提供一些代码上使用的缓存
 **/
@EnableCaching
@Configuration
class CacheConfiguration {

    companion object {
        /**
         * 系统默认缓存TTL时间：4分钟
         * 一些需要用到缓存的数据，譬如支付单，需要按此数据来规划过期时间
         */
        const val SYSTEM_DEFAULT_EXPIRES: Long = 4 * 6 * 1000
    }

    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager().apply {
        setCaffeine(
            Caffeine.newBuilder()
                .expireAfterWrite(SYSTEM_DEFAULT_EXPIRES, TimeUnit.MILLISECONDS)
        )
    }

    @Bean(name = ["settlement"])
    fun getSettlementTTLCache(): Cache = CaffeineCache(
        "settlement",
        Caffeine.newBuilder()
            .expireAfterAccess(CacheConfiguration.SYSTEM_DEFAULT_EXPIRES, TimeUnit.MILLISECONDS)
            .build()
    )

}

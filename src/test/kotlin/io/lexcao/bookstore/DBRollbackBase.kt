package io.lexcao.bookstore

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

/**
 * 单元测试基类
 * <p>
 * 提供了每个单元测试自动恢复数据库、清理缓存的处理
 **/
@ActiveProfiles("test")
@SpringBootTest(classes = [BookstoreApplication::class])
@Sql(scripts = ["classpath:db/hsqldb/schema.sql", "classpath:db/hsqldb/data.sql"])
abstract class DBRollbackBase {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @BeforeEach
    fun evictAllCaches() {
        for (name in cacheManager.cacheNames) {
            cacheManager.getCache(name)?.clear()
        }
    }
}

package io.lexcao.bookstore.domain.account

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.CrudRepository
import java.util.Optional

/**
 * 用户对象数据仓库
 */
@CacheConfig(cacheNames = ["repository.account"])
interface AccountRepository : CrudRepository<Account, Int> {

    @Cacheable(key = "#username")
    fun findByUsername(username: String): Account?

    /**
     * 判断唯一性，用户名、邮箱、电话不允许任何一个重复
     */
    fun existsByUsernameOrEmailOrTelephone(username: String, email: String, telephone: String): Boolean

    /**
     * 判断唯一性，用户名、邮箱、电话不允许任何一个重复
     */
    fun findByUsernameOrEmailOrTelephone(username: String, email: String, telephone: String): Collection<Account>

    /**
     * 判断存在性，用户名存在即为存在
     */
    @Cacheable(key = "#username")
    fun existsByUsername(username: String): Boolean

    // 覆盖以下父类中需要处理缓存失效的方法
    // 父类取不到CacheConfig的配置信息，所以不能抽象成一个通用的父类接口中完成
    @Caching(evict = [CacheEvict(key = "#entity.id"), CacheEvict(key = "#entity.username")])
    override fun <S : Account> save(entity: S): S

    @CacheEvict
    override fun <S : Account> saveAll(entities: Iterable<S>): Iterable<S>

    @Cacheable(key = "#id")
    override fun findById(id: Int): Optional<Account>

    @Cacheable(key = "#id")
    override fun existsById(id: Int): Boolean

    @CacheEvict(key = "#id")
    override fun deleteById(id: Int)

    @CacheEvict(key = "#entity.id")
    override fun delete(entity: Account)

    @CacheEvict(allEntries = true)
    override fun deleteAll(entities: Iterable<Account>)

    @CacheEvict(allEntries = true)
    override fun deleteAll()
}

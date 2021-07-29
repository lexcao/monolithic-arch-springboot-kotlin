package io.lexcao.bookstore.domain.warehouse

import org.springframework.data.repository.CrudRepository

/**
 * 商品对象数据仓库
 */
interface ProductRepository : CrudRepository<Product, Int> {
    fun findByIdIn(ids: Collection<Int>): Collection<Product>
}

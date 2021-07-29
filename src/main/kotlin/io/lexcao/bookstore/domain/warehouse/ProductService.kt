package io.lexcao.bookstore.domain.warehouse

import io.lexcao.bookstore.application.payment.dto.Settlement
import org.springframework.data.repository.findByIdOrNull
import javax.inject.Named

/**
 * 产品领域服务
 */
@Named
class ProductService(
    val repository: ProductRepository
) {

    /**
     * 根据结算单中货物的ID，填充货物的完整信息到结算单对象上
     */
    fun replenishProductInformation(bill: Settlement) {
        val ids = bill.items.map(Settlement.Item::productId)
        bill.productMap = repository.findByIdIn(ids).associateBy(Product::id)
    }

    /**
     * 获取仓库中所有的货物信息
     */
    fun allProducts(): Iterable<Product> = repository.findAll()

    /**
     * 获取仓库中指定的货物信息
     */
    fun getProduct(id: Int): Product? = repository.findByIdOrNull(id)

    /**
     * 创建或者更新产品信息
     */
    fun saveProduct(product: Product): Product {
        return repository.save(product)
    }

    /**
     * 删除指定产品
     */
    fun removeProduct(id: Int) {
        repository.deleteById(id)
    }
}

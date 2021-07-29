package io.lexcao.bookstore.application

import io.lexcao.bookstore.domain.payment.Stockpile
import io.lexcao.bookstore.domain.payment.StockpileService
import io.lexcao.bookstore.domain.warehouse.Product
import io.lexcao.bookstore.domain.warehouse.ProductService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 产品的应用服务接口
 */
@Service
@Transactional
class ProductApplicationService(
    val service: ProductService,
    val stockpileService: StockpileService
) {

    /**
     * 获取仓库中所有的货物信息
     */
    fun allProducts(): Iterable<Product> = service.allProducts()

    /**
     * 获取仓库中指定的货物信息
     */
    fun getProduct(id: Int): Product? {
        return service.getProduct(id)
    }

    /**
     * 创建或更新产品信息
     */
    fun saveProduct(product: Product): Product {
        return service.saveProduct(product)
    }

    /**
     * 删除指定产品
     */
    fun removeProduct(id: Int) {
        service.removeProduct(id)
    }

    /**
     * 根据产品查询库存
     */
    fun getStockpile(productId: Int): Stockpile {
        return stockpileService.getByProductId(productId)
    }

    /**
     * 将指定的产品库存调整为指定数额
     */
    fun setStockpileAmountByProductId(productId: Int, amount: Int) {
        stockpileService.setAmount(productId, amount)
    }
}

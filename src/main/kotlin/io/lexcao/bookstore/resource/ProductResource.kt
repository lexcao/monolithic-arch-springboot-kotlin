package io.lexcao.bookstore.resource

import io.lexcao.bookstore.application.ProductApplicationService
import io.lexcao.bookstore.domain.auth.Role
import io.lexcao.bookstore.domain.payment.Stockpile
import io.lexcao.bookstore.domain.warehouse.Product
import io.lexcao.bookstore.infrastructure.jaxrs.CommonResponse
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Component
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.PATCH
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 产品相关的资源
 */
@Path("/products")
@Component
@CacheConfig(cacheNames = ["resource.product"])
@Produces(MediaType.APPLICATION_JSON)
class ProductResource(
    val service: ProductApplicationService
) {

    /**
     * 获取仓库中所有的货物信息
     */
    @GET
    @Cacheable(key = "'ALL_PRODUCT'")
    fun allProducts(): Iterable<Product> = service.allProducts()

    /**
     * 获取仓库中指定的货物信息
     */
    @GET
    @Path("/{id}")
    @Cacheable(key = "#id")
    fun getProduct(@PathParam("id") id: Int): Product? = service.getProduct(id)

    /**
     * 更新产品信息
     */
    @PUT
    @Caching(evict = [CacheEvict(key = "#product.id"), CacheEvict(key = "'ALL_PRODUCT'")])
    @RolesAllowed(Role.ADMIN)
    fun updateProduct(@Valid product: Product): Response {
        return CommonResponse.op { service.saveProduct(product) }
    }

    /**
     * 创建新的产品
     */
    @POST
    @Caching(evict = [CacheEvict(key = "#product.id"), CacheEvict(key = "'ALL_PRODUCT'")])
    @RolesAllowed(Role.ADMIN)
    fun createProduct(@Valid product: Product): Product {
        return service.saveProduct(product)
    }

    /**
     * 创建新的产品
     */
    @DELETE
    @Path("/{id}")
    @Caching(evict = [CacheEvict(key = "#id"), CacheEvict(key = "'ALL_PRODUCT'")])
    @RolesAllowed(Role.ADMIN)
    fun removeProduct(@PathParam("id") id: Int): Response {
        return CommonResponse.op { service.removeProduct(id) }
    }

    /**
     * 将指定的产品库存调整为指定数额
     */
    @PATCH
    @Path("/stockpile/{productId}")
    @RolesAllowed(Role.ADMIN)
    fun updateStockpile(
        @PathParam("productId") productId: Int,
        @QueryParam("amount") amount: Int,
    ): Response = CommonResponse.op {
        service.setStockpileAmountByProductId(productId, amount)
    }

    /**
     * 根据产品查询库存
     */
    @GET
    @Path("/stockpile/{productId}")
    @RolesAllowed(Role.ADMIN)
    fun queryStockpile(@PathParam("productId") productId: Int): Stockpile {
        return service.getStockpile(productId)
    }
}

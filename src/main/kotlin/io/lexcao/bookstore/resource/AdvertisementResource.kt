package io.lexcao.bookstore.resource

import io.lexcao.bookstore.domain.warehouse.Advertisement
import io.lexcao.bookstore.domain.warehouse.AdvertisementRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * 广告相关的资源
 */
@Path("/advertisements")
@Component
@Produces(MediaType.APPLICATION_JSON)
class AdvertisementResource(
    val repository: AdvertisementRepository
) {

    @GET
    @Cacheable("resource.advertisements")
    fun getAllAdvertisements(): Iterable<Advertisement> = repository.findAll()
}

package io.lexcao.bookstore.domain.warehouse

import org.springframework.data.repository.CrudRepository

/**
 * 广告对象数据仓库
 **/
interface AdvertisementRepository : CrudRepository<Advertisement, Int>

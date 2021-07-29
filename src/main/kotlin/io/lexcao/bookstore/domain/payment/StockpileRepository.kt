package io.lexcao.bookstore.domain.payment

import org.springframework.data.repository.CrudRepository

/**
 * 库存数据仓库
 */
interface StockpileRepository : CrudRepository<Stockpile, Int>

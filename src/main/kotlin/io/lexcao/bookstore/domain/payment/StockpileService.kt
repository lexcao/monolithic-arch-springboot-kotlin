package io.lexcao.bookstore.domain.payment

import mu.KLogging
import org.springframework.data.repository.findByIdOrNull
import javax.inject.Named
import javax.persistence.EntityNotFoundException

/**
 * 商品库存的领域服务
 */
@Named
class StockpileService(
    val repository: StockpileRepository
) {

    companion object : KLogging()

    /**
     * 根据产品查询库存
     */
    fun getByProductId(productId: Int): Stockpile {
        return repository.findByIdOrNull(productId)
            ?: throw EntityNotFoundException(productId.toString())
    }

    /**
     * 货物售出
     * 从冻结状态的货物中扣减
     */
    fun decrease(productId: Int, amount: Int) {
        val stock = getByProductId(productId)
        stock.decrease(amount)
        repository.save(stock)
        logger.info("库存出库，商品：{}，数量：{}", productId, amount)
    }

    /**
     * 货物增加
     * 增加指定数量货物至正常货物状态
     */
    fun increase(productId: Int, amount: Int) {
        val stock = getByProductId(productId)
        stock.increase(amount)
        repository.save(stock)
        logger.info("库存入库，商品：{}，数量：{}", productId, amount)
    }

    /**
     * 货物冻结
     * 从正常货物中移动指定数量至冻结状态
     */
    fun frozen(productId: Int, amount: Int) {
        val stock = getByProductId(productId)
        stock.frozen(amount)
        repository.save(stock)
        logger.info("冻结库存，商品：{}，数量：{}", productId, amount)
    }

    /**
     * 货物解冻
     * 从冻结货物中移动指定数量至正常状态
     */
    fun thawed(productId: Int, amount: Int) {
        val stock = getByProductId(productId)
        stock.thawed(amount)
        repository.save(stock)
        logger.info("解冻库存，商品：{}，数量：{}", productId, amount)
    }

    /**
     * 设置货物数量
     */
    fun setAmount(productId: Int, amount: Int) {
        val stock = getByProductId(productId)
        stock.amount = amount
        repository.save(stock)
    }

}

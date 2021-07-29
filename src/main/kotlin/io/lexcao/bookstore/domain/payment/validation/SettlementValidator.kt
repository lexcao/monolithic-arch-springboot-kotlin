package io.lexcao.bookstore.domain.payment.validation

import io.lexcao.bookstore.application.payment.dto.Settlement
import io.lexcao.bookstore.domain.payment.StockpileService
import javax.inject.Inject
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 结算单验证器
 *
 *
 * 结算单能够成功执行的约束是清单中每一项商品的库存量都足够。
 *
 *
 * 这个验证器的目的不在于保证商品高并发情况（如秒杀活动）下不超卖，而在于避免库存不足时仍可下单。高并发下的超卖是一种“不可重复读”现象
 * （即读取过的数据在事务期间被另一个事务改变），如要严谨地避免，需要把数据库的隔离级别从默认的“Read Committed”提升至“Repeatable Read”
 * 除了MySQL（InnoDB）外，主流的数据库，如Oracle、SQLServer默认都是Read committed，提升隔离级别会显著影响数据库的并发能力。
 */
class SettlementValidator : ConstraintValidator<SufficientStock, Settlement> {

    @Inject
    lateinit var service: StockpileService

    override fun isValid(value: Settlement, context: ConstraintValidatorContext): Boolean {
        return value.items.none { service.getByProductId(it.productId).amount < it.amount }
    }
}

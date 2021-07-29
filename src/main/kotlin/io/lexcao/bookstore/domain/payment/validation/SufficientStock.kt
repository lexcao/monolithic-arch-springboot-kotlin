package io.lexcao.bookstore.domain.payment.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 判断结算单中货物存量是充足的
 *
 * @author icyfenix@gmail.com
 * @date 2020/3/16 8:59
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Constraint(validatedBy = [SettlementValidator::class])
annotation class SufficientStock(
    val message: String = "商品库存不足",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

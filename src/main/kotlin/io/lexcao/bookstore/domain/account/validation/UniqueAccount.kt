package io.lexcao.bookstore.domain.account.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 表示一个用户是唯一的
 *
 *
 * 唯一不仅仅是用户名，还要求手机、邮箱均不允许重复
 */
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Constraint(validatedBy = [AccountValidation.UniqueAccountValidator::class])
annotation class UniqueAccount(
    val message: String = "用户名称、邮箱、手机号码均不允许与现存用户重复",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

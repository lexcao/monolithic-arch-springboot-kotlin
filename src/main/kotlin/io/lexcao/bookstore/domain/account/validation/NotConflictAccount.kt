package io.lexcao.bookstore.domain.account.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 表示一个用户的信息是无冲突的
 *
 *
 * “无冲突”是指该用户的敏感信息与其他用户不重合，譬如将一个注册用户的邮箱，修改成与另外一个已存在的注册用户一致的值，这便是冲突
 */
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Constraint(validatedBy = [AccountValidation.NotConflictAccountValidator::class])
annotation class NotConflictAccount(
    val message: String = "用户名称、邮箱、手机号码与现存用户产生重复",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

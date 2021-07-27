package io.lexcao.bookstore.domain.account.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

/**
 * 代表用户必须与当前登陆的用户一致
 * 相当于使用Spring Security的@PreAuthorize("#{user.name == authentication.name}")的验证
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Constraint(validatedBy = [AccountValidation.AuthenticatedAccountValidator::class])
annotation class AuthenticatedAccount(
    val message: String = "不是当前登陆用户",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

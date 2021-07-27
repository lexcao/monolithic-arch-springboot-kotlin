package io.lexcao.bookstore.domain.account.validation

import com.github.fenixsoft.bookstore.domain.auth.AuthenticAccount
import io.lexcao.bookstore.domain.account.Account
import io.lexcao.bookstore.domain.account.AccountRepository
import org.springframework.security.core.context.SecurityContextHolder
import javax.inject.Inject
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 用户对象校验器
 *
 *
 * 如，新增用户时，判断该用户对象是否允许唯一，在修改用户时，判断该用户是否存在
 */
sealed class AccountValidation<T : Annotation> : ConstraintValidator<T, Account> {

    @Inject
    protected lateinit var repository: AccountRepository

    protected lateinit var predicate: (Account) -> Boolean

    override fun isValid(value: Account, context: ConstraintValidatorContext): Boolean {
        // 在JPA持久化时，默认采用Hibernate实现，插入、更新时都会调用BeanValidationEventListener进行验证
        // 而验证行为应该尽可能在外层进行，Resource中已经通过@Vaild注解触发过一次验证，这里会导致重复执行
        // 正常途径是使用分组验证避免，但@Vaild不支持分组，@Validated支持，却又是Spring的私有标签
        // 另一个途径是设置Hibernate配置文件中的javax.persistence.validation.mode参数为“none”，这个参数在Spring的yml中未提供桥接
        // 为了避免涉及到数据库操作的验证重复进行，在这里做增加此空值判断，利用Hibernate验证时验证器不是被Spring创建的特点绕开
        return !this::repository.isInitialized || predicate(value)
    }

    class ExistsAccountValidator : AccountValidation<ExistsAccount>() {
        override fun initialize(constraintAnnotation: ExistsAccount) {
            predicate = { repository.existsById(it.id) }
        }
    }

    class AuthenticatedAccountValidator : AccountValidation<AuthenticatedAccount>() {
        override fun initialize(constraintAnnotation: AuthenticatedAccount) {
            predicate = {
                val principal = SecurityContextHolder.getContext().authentication.principal
                if ("anonymousUser" == principal) {
                    false
                } else {
                    it.id == (principal as AuthenticAccount).id
                }
            }
        }
    }

    class UniqueAccountValidator : AccountValidation<UniqueAccount>() {
        override fun initialize(constraintAnnotation: UniqueAccount) {
            predicate = { !repository.existsByUsernameOrEmailOrTelephone(it.username, it.email, it.telephone) }
        }
    }

    class NotConflictAccountValidator : AccountValidation<NotConflictAccount>() {
        override fun initialize(constraintAnnotation: NotConflictAccount) {
            predicate = {
                val collection = repository.findByUsernameOrEmailOrTelephone(it.username, it.email, it.telephone)
                collection.isEmpty() || collection.size == 1 && collection.iterator().next().id == it.id
            }
        }
    }
}

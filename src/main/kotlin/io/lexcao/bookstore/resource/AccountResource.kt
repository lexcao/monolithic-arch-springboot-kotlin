package io.lexcao.bookstore.resource

import io.lexcao.bookstore.application.AccountApplicationService
import io.lexcao.bookstore.domain.account.Account
import io.lexcao.bookstore.domain.account.validation.AuthenticatedAccount
import io.lexcao.bookstore.domain.account.validation.NotConflictAccount
import io.lexcao.bookstore.domain.account.validation.UniqueAccount
import io.lexcao.bookstore.infrastructure.jaxrs.CommonResponse
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 用户资源
 *
 *
 * 对客户端以Restful形式暴露资源，提供对用户资源[Account]的管理入口
 */
@Path("/accounts")
@Component
@CacheConfig(cacheNames = ["resource.account"])
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AccountResource(
    val service: AccountApplicationService
) {

    /**
     * 根据用户名称获取用户详情
     */
    @GET
    @Path("/{username}")
    @Cacheable(key = "#username")
    fun getUser(@PathParam("username") username: String): Account? {
        return service.findAccountByUsername(username)
    }

    /**
     * 创建新的用户
     */
    @POST
    @CacheEvict(key = "#user.username")
    fun createUser(@Valid @UniqueAccount user: Account): Response {
        return CommonResponse.op { service.createAccount(user) }
    }

    /**
     * 更新用户信息
     */
    @PUT
    @CacheEvict(key = "#user.username")
    fun updateUser(@Valid @AuthenticatedAccount @NotConflictAccount user: Account): Response {
        return CommonResponse.op { service.updateAccount(user) }
    }
}

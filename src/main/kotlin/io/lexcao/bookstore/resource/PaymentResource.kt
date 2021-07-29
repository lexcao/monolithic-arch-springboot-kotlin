package io.lexcao.bookstore.resource

import io.lexcao.bookstore.application.payment.PaymentApplicationService
import io.lexcao.bookstore.domain.auth.AuthenticAccount
import io.lexcao.bookstore.domain.auth.Role
import io.lexcao.bookstore.domain.payment.Payment
import io.lexcao.bookstore.infrastructure.jaxrs.CommonResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.PATCH
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 支付单相关的资源
 */
@Path("/pay")
@Component
@Produces(MediaType.APPLICATION_JSON)
class PaymentResource(
    val service: PaymentApplicationService
) {

    /**
     * 修改支付单据的状态
     */
    @PATCH
    @Path("/{payId}")
    @RolesAllowed(Role.USER)
    fun updatePaymentState(
        @PathParam("payId") payId: String,
        @QueryParam("state") state: Payment.State
    ): Response {
        val account = SecurityContextHolder.getContext().authentication.principal as AuthenticAccount
        return updatePaymentStateAlias(payId, account.id, state)
    }

    /**
     * 修改支付单状态的GET方法别名
     * 考虑到该动作要由二维码扫描来触发，只能进行GET请求，所以增加一个别名以便通过二维码调用
     * 这个方法原本应该作为银行支付接口的回调，不控制调用权限（谁付款都行），但都认为是购买用户付的款
     */
    @GET
    @Path("/modify/{payId}")
    fun updatePaymentStateAlias(
        @PathParam("payId") payId: String,
        @QueryParam("accountId") accountId: Int,
        @QueryParam("state") state: Payment.State
    ): Response = CommonResponse.op {
        if (state == Payment.State.PAYED) {
            service.accomplishPayment(accountId, payId)
        } else {
            service.cancelPayment(payId)
        }
    }
}

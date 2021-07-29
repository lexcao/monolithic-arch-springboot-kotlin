package io.lexcao.bookstore.resource

import io.lexcao.bookstore.application.payment.PaymentApplicationService
import io.lexcao.bookstore.application.payment.dto.Settlement
import io.lexcao.bookstore.domain.auth.Role
import io.lexcao.bookstore.domain.payment.Payment
import io.lexcao.bookstore.domain.payment.validation.SufficientStock
import org.springframework.stereotype.Component
import javax.annotation.security.RolesAllowed
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * 结算清单相关的资源
 */
@Path("/settlements")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class SettlementResource(
    val service: PaymentApplicationService
) {

    /**
     * 提交一张交易结算单，根据结算单中的物品，生成支付单
     */
    @POST
    @RolesAllowed(Role.USER)
    fun executeSettlement(@Valid @SufficientStock settlement: Settlement): Payment {
        return service.executeBySettlement(settlement)
    }
}

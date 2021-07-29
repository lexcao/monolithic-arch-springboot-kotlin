package io.lexcao.bookstore.resource

import io.lexcao.bookstore.application.payment.dto.Settlement
import io.lexcao.bookstore.domain.payment.Payment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class PaymentResourceTest : JAXRSResourceBase() {

    private fun settlement() = Settlement(
        items = listOf(
            Settlement.Item(
                amount = 2,
                productId = 1
            )
        ),
        purchase = Settlement.Purchase(
            location = "xx rd. zhuhai. guangdong. china",
            name = "icyfenix",
            pay = "wechat",
            telephone = "18888888888"
        )
    )

    @Test
    fun executeSettlement() {
        val settlement = settlement()
        assertForbidden(post("/settlements", settlement))
        authenticated {
            val response = post("/settlements", settlement)
            assertOK(response)
            val payment = response.readEntity(
                Payment::class.java
            )
            Assertions.assertNotNull(payment.payId)
        }
    }

    @Test
    fun updatePaymentState() {
        val settlement = settlement()
        authenticated {
            var payment = post("/settlements", settlement).readEntity(
                Payment::class.java
            )
            assertOK(patch("/pay/" + payment.payId + "?state=PAYED"))
            assertServerError(patch("/pay/" + payment.payId + "?state=CANCEL"))
            payment = post("/settlements", settlement).readEntity(Payment::class.java) // another
            assertOK(patch("/pay/" + payment.payId + "?state=CANCEL"))
            assertServerError(patch("/pay/" + payment.payId + "?state=NOT_SUPPORT"))
        }
    }

    @Test
    fun updatePaymentStateAlias() {
        val payment = authenticated {
            post("/settlements", settlement()).readEntity(
                Payment::class.java
            )
        }
        assertOK(get(payment.paymentLink))
    }
}

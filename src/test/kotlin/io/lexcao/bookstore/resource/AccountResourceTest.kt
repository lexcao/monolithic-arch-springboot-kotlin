package io.lexcao.bookstore.resource

import io.lexcao.bookstore.domain.account.Account
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author icyfenix@gmail.com
 * @date 2020/4/6 18:52
 */
internal class AccountResourceTest : JAXRSResourceBase() {

    @Test
    fun userWithExistAccount() {
        val resp = get("/accounts/icyfenix")
        assertOK(resp)
        val icyfenix = resp.readEntity(Account::class.java)
        Assertions.assertEquals("icyfenix", icyfenix.username, "should return user: icyfenix")
    }

    @Test
    fun userWithNotExistAccount() {
        assertNoContent(get("/accounts/nobody"))
    }

    @Test
    fun createUser() {
        val newbeeShouldFailure = Account(
            username = "newbee",
            email = "newbee@github.com"
        )
        assertBadRequest(post("/accounts", newbeeShouldFailure))

        val newbeeShouldSuccess = newbeeShouldFailure.copy(
            telephone = "13888888888"
        )
        newbeeShouldSuccess.name = "somebody"
        assertNoContent(get("/accounts/newbee"))
        assertOK(post("/accounts", newbeeShouldSuccess))
        assertOK(get("/accounts/newbee"))
    }

    @Test
    fun updateUser() {
        authenticated {
            val resp = get("/accounts/icyfenix")
            val icyfenix = resp.readEntity(Account::class.java)
            icyfenix.name = "zhouzhiming"
            assertOK(put("/accounts", icyfenix))
            Assertions.assertEquals("zhouzhiming",
                get("/accounts/icyfenix").readEntity(Account::class.java).name,
                "should get the new name now")
        }
    }
}

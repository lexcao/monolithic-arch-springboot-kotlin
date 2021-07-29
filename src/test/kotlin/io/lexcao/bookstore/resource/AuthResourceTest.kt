package io.lexcao.bookstore.resource

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.UriBuilder

class AuthResourceTest : JAXRSResourceBase() {

    @Test
    fun refreshToken() {
        val refreshToken = login().getString("refresh_token")
        val url = UriBuilder.fromPath("http://localhost:$port/oauth/token")
            .queryParam("refresh_token", refreshToken)
            .queryParam("grant_type", "refresh_token")
            .queryParam("client_id", "bookstore_frontend")
            .queryParam("client_secret", "bookstore_secret")
        val resp = ClientBuilder.newClient().target(url).request().get()
        val accessToken = json(resp).getString("access_token")
        Assertions.assertNotNull(accessToken)
    }
}

package io.lexcao.bookstore.resource

import org.junit.jupiter.api.Test

class AdvertisementResourceTest : JAXRSResourceBase() {
    @Test
    fun getAllAdvertisements() {
        assertOK(get("/advertisements"))
    }
}

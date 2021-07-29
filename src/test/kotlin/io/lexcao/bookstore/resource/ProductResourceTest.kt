package io.lexcao.bookstore.resource

import io.lexcao.bookstore.domain.payment.Stockpile
import io.lexcao.bookstore.domain.warehouse.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ProductResourceTest : JAXRSResourceBase() {

    @Test
    fun allProducts() {
        assertOK(get("/products"))
    }

    @Test
    fun product() {
        assertOK(get("/products/1"))
        assertNoContent(get("/products/10086"))
        val book = get("/products/1").readEntity(Product::class.java)
        Assertions.assertEquals("深入理解Java虚拟机（第3版）", book.title)
    }

    @Test
    fun updateProduct() {
        val book = get("/products/1").readEntity(Product::class.java)
        book.title = "深入理解Java虚拟机（第4版）"
        assertForbidden(put("/products", book))
        authenticated {
            assertOK(put("/products", book))
        }
        val modifiedBook = get("/products/1").readEntity(Product::class.java)
        Assertions.assertEquals("深入理解Java虚拟机（第4版）", modifiedBook.title)
    }

    @Test
    fun createProduct() {
        val book = Product("new book")
        book.price = 50.0
        book.rate = 8.0
        assertForbidden(post("/products", book))
        authenticated {
            val response = post("/products", book)
            assertOK(response)
            val fetchBook = response.readEntity(Product::class.java)
            Assertions.assertEquals(book.title, fetchBook.title)
            Assertions.assertNotNull(fetchBook.id)
        }
    }

    @Test
    fun removeProduct() {
        val number = jsonArray(get("/products")).length()
        assertForbidden(delete("/products/1"))
        authenticated {
            assertOK(delete("/products/1"))
        }
        Assertions.assertEquals(number - 1, jsonArray(get("/products")).length())
    }

    @Test
    fun updateAndQueryStockpile() {
        authenticated {
            assertOK(patch("/products/stockpile/1?amount=20"))
            val stockpile = get("/products/stockpile/1").readEntity(Stockpile::class.java)
            Assertions.assertEquals(20, stockpile.amount)
        }
    }
}

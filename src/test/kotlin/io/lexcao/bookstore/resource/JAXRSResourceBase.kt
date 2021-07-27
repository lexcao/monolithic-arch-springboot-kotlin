package io.lexcao.bookstore.resource

import io.lexcao.bookstore.BookstoreApplication
import io.lexcao.bookstore.DBRollbackBase
import org.glassfish.jersey.client.HttpUrlConnectorProvider
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 单元测试基类
 * <p>
 * 提供对JAX-RS资源的HTTP访问方法、登录授权、JSON字符串访问等支持
 **/
@SpringBootTest(classes = [BookstoreApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JAXRSResourceBase : DBRollbackBase() {

    @Value("\${local.server.port}")
    var port: Int = 0

    fun build(path: String): Invocation.Builder = ClientBuilder.newClient()
        .target("http://localhost:$port/restful$path")
        .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
        .request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)

    fun get(path: String): Response = build(path).get()

    fun delete(path: String): Response = build(path).delete()

    fun post(path: String, body: Any): Response = build(path).post(Entity.json(body))

    fun put(path: String, body: Any): Response = build(path).put(Entity.json(body))

    fun patch(path: String): Response = build(path).method("PATCH", Entity.text("MUST_BE_PRESENT"))

    companion object {
        fun assertOK(response: Response) {
            Assertions.assertEquals(
                Response.Status.OK.statusCode,
                response.status,
                "期望HTTP Status Code应为：200/OK"
            )
        }

        fun assertNoContent(response: Response) {
            Assertions.assertEquals(
                Response.Status.NO_CONTENT.statusCode,
                response.status,
                "期望HTTP Status Code应为：204/NO_CONTENT"
            )
        }

        fun assertBadRequest(response: Response) {
            Assertions.assertEquals(
                Response.Status.BAD_REQUEST.statusCode,
                response.status,
                "期望HTTP Status Code应为：400/BAD_REQUEST"
            )
        }

        fun assertForbidden(response: Response) {
            Assertions.assertEquals(
                Response.Status.FORBIDDEN.statusCode,
                response.status,
                "期望HTTP Status Code应为：403/FORBIDDEN"
            )
        }

        fun assertServerError(response: Response) {
            Assertions.assertEquals(
                Response.Status.INTERNAL_SERVER_ERROR.statusCode,
                response.status,
                "期望HTTP Status Code应为：500/INTERNAL_SERVER_ERROR"
            )
        }

        fun assertNotFound(response: Response) {
            Assertions.assertEquals(
                Response.Status.NOT_FOUND.statusCode,
                response.status,
                "期望HTTP Status Code应为：404/NOT_FOUND"
            )
        }
    }
}

package io.lexcao.bookstore.resource

import io.lexcao.bookstore.BookstoreApplication
import io.lexcao.bookstore.DBRollbackBase
import org.glassfish.jersey.client.HttpUrlConnectorProvider
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriBuilder

/**
 * 单元测试基类
 * <p>
 * 提供对JAX-RS资源的HTTP访问方法、登录授权、JSON字符串访问等支持
 **/
@SpringBootTest(classes = [BookstoreApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JAXRSResourceBase : DBRollbackBase() {

    @Value("\${local.server.port}")
    var port: Int = 0

    private var accessToken: String? = null

    fun build(path: String): Invocation.Builder = ClientBuilder.newClient()
        .target("http://localhost:$port/restful$path")
        .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
        .request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .apply {
            if (accessToken != null) {
                header("Authorization", "bearer $accessToken")
            }
        }

    fun json(response: Response): JSONObject {
        return JSONObject(response.readEntity(String::class.java))
    }

    fun jsonArray(response: Response): JSONArray {
        return JSONArray(response.readEntity(String::class.java))
    }

    fun <T> authenticated(block: () -> T): T {
        try {
            login()
            return block()
        } finally {
            logout()
        }
    }

    fun logout() {
        accessToken = null
    }

    /**
     * 单元测试中登陆固定使用icyfenix这个用户
     */
    fun login(): JSONObject {
        val url = UriBuilder.fromPath("http://localhost:$port/oauth/token")
            .queryParam("username", "icyfenix")
            .queryParam("password", "MFfTW3uNI4eqhwDkG7HP9p2mzEUu%2Fr2")
            .queryParam("grant_type", "password")
            .queryParam("client_id", "bookstore_frontend")
            .queryParam("client_secret", "bookstore_secret")
        val resp = ClientBuilder.newClient().target(url).request().get()
        return json(resp).also {
            accessToken = it.getString("access_token")
        }
    }

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

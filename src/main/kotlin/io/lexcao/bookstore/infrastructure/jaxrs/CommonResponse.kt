package io.lexcao.bookstore.infrastructure.jaxrs

import mu.KLogging
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * 为了简化编码而设计的HTTP Response对象包装类和工具集
 *
 *
 * 带有服务状态编码的（带有Code字段的）JavaBean领域对象包装类
 * Code字段的通常用于服务消费者判定该请求的业务处理是否成功。
 *
 *
 * 统一约定：
 * - 当服务调用正常完成，返回Code一律以0表示
 * - 当服务调用产生异常，可自定义不为0的Code值，此时Message字段作为返回客户端的详细信息
 */
object CommonResponse : KLogging() {

    /**
     * 向客户端发送自定义操作信息
     */
    fun send(status: Response.Status, message: String): Response {
        val code: Int = if (status.family == Response.Status.Family.SUCCESSFUL) CodedMessage.CODE_SUCCESS else CodedMessage.CODE_DEFAULT_FAILURE
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(CodedMessage(code, message)).build()
    }

    /**
     * 向客户端发送操作失败的信息
     */
    fun failure(message: String): Response {
        return send(Response.Status.INTERNAL_SERVER_ERROR, message)
    }

    /**
     * 向客户端发送操作成功的信息
     */
    fun success(message: String): Response {
        return send(Response.Status.OK, message)
    }

    /**
     * 向客户端发送操作成功的信息
     */
    fun success(): Response {
        return send(Response.Status.OK, "操作已成功")
    }

    /**
     * 执行操作，并根据操作是否成功返回给客户端相应信息
     * 封装了在服务端接口中很常见的执行操作，成功返回成功标志、失败返回失败标志的通用操作，用于简化编码
     */
    fun op(
        onException: (Exception) -> Unit = { e -> logger.error(e.message, e) },
        block: () -> Unit
    ): Response = try {
        block()
        success()
    } catch (e: Exception) {
        onException(e)
        failure(e.localizedMessage)
    }
}

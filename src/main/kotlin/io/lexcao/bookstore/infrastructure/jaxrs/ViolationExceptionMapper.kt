package io.lexcao.bookstore.infrastructure.jaxrs

import mu.KLogging
import javax.validation.ConstraintViolationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * 用于统一处理在Resource中由于验证器验证失败而带回客户端的错误信息
 */
@Provider
class ViolationExceptionMapper : ExceptionMapper<ConstraintViolationException> {

    companion object : KLogging()

    override fun toResponse(exception: ConstraintViolationException): Response {
        logger.warn("客户端传入了校验结果为非法的数据", exception)
        val msg = exception.constraintViolations.joinToString("；") { it.message }
        return CommonResponse.send(Response.Status.BAD_REQUEST, msg)
    }
}

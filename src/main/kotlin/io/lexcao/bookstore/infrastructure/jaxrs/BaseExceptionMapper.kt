package io.lexcao.bookstore.infrastructure.jaxrs

import mu.KLogging
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * 用于兜底的全局处理器，如果其他所有的Mapper都不合适，将由此处理把错误带到前端
 */
@Provider
class BaseExceptionMapper : ExceptionMapper<Throwable> {

    companion object : KLogging()

    override fun toResponse(exception: Throwable): Response {
        logger.error(exception.message, exception)
        return CommonResponse.failure(exception.localizedMessage)
    }
}

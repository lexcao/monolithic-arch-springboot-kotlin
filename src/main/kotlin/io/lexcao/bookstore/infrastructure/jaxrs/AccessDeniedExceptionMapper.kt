package io.lexcao.bookstore.infrastructure.jaxrs

import mu.KLogging
import org.springframework.security.access.AccessDeniedException
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * 用于统一处理在Resource中由于Spring Security授权访问产生的异常信息
 */
@Provider
class AccessDeniedExceptionMapper : ExceptionMapper<AccessDeniedException> {

    companion object : KLogging()

    @Context
    private lateinit var request: HttpServletRequest

    override fun toResponse(exception: AccessDeniedException): Response {
        logger.warn("越权访问被禁止 {}: {}", request.method, request.pathInfo)
        return CommonResponse.send(Response.Status.FORBIDDEN, exception.localizedMessage)
    }
}

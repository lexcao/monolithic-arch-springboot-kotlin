/*
 * Copyright 2012-2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. More information from:
 *
 *        https://github.com/fenixsoft
 */
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

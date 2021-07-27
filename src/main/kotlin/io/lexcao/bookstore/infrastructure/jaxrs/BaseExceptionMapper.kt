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

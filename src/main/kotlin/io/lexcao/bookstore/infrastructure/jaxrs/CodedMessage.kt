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

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * 带编码的实体容器
 *
 *
 * 一般来说REST服务应采用HTTP Status Code带回错误信息编码
 * 但很多前端开发都习惯以JSON-RPC的风格处理异常，所以仍然保留这个编码容器
 * 用于返回给客户端以形式为“{code,message,data}”的对象格式
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CodedMessage(
    val code: Int,
    val message: String,
    val data: Any? = null
) {
    companion object {
        /**
         * 约定的成功标志
         */
        const val CODE_SUCCESS = 0

        /**
         * 默认的失败标志，其他失败含义可以自定义
         */
        const val CODE_DEFAULT_FAILURE = 1
    }
}

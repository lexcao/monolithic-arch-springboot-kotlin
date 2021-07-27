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

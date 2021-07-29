package io.lexcao.bookstore.domain.account

import com.fasterxml.jackson.annotation.JsonProperty
import io.lexcao.bookstore.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

/**
 * 用户实体
 */
@Entity
data class Account(
    @field:NotEmpty(message = "用户名不允许为空")
    val username: String = "",
    @field:Pattern(regexp = "1\\d{10}", message = "手机号格式不正确")
    val telephone: String = "",
    @field:Email
    val email: String = "",
    override val id: Int = 0
) : BaseEntity() {

    @field:NotEmpty(message = "用户姓名不允许为空")
    var name: String = ""

    // 密码字段不参与序列化（但反序列化是参与的）、不参与更新（但插入是参与的）
    // 这意味着密码字段不会在获取对象（很多操作都会关联用户对象）的时候泄漏出去；
    // 也意味着此时“修改密码”一类的功能无法以用户对象资源的接口来处理（因为更新对象时密码不会被更新），需要单独提供接口去完成
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(updatable = false)
    var password: String = ""
    var avatar: String = ""
    var location: String = ""
}

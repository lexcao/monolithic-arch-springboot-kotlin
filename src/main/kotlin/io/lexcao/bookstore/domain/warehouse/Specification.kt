package io.lexcao.bookstore.domain.warehouse

import io.lexcao.bookstore.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

/**
 * 商品规格
 */
@Entity
data class Specification(
    @field:NotEmpty(message = "商品规格名称不允许为空")
    var item: String = "",
    @field:NotEmpty(message = "商品规格内容不允许为空")
    var value: String = "",
    @Column(name = "product_id")
    @field:Min(value = 1, message = "商品规格必须归属于指定商品")
    val productId: Int = 0
) : BaseEntity()

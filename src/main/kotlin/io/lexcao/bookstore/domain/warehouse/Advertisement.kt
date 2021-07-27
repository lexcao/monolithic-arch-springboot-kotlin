package io.lexcao.bookstore.domain.warehouse

import io.lexcao.bookstore.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotEmpty

/**
 * 广告对象模型
 **/
@Entity
data class Advertisement(
    @Column(name = "product_id")
    val productId: Int = 0,

    @field:NotEmpty
    val image: String = ""
) : BaseEntity()

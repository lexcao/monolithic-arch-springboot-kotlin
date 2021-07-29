package io.lexcao.bookstore.domain.warehouse

import io.lexcao.bookstore.domain.BaseEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

/**
 * 商品对象模型
 */
@Entity
data class Product(
    @field:NotEmpty(message = "商品名称不允许为空")
    var title: String = ""
) : BaseEntity() {
    // 这里是偷懒，正式场合使用BigDecimal来表示金额
    @field:Min(value = 0, message = "商品价格最低为零")
    var price: Double = 0.0

    @field:Min(value = 0, message = "评分最低为0")
    @field:Max(value = 10, message = "评分最高为10")
    var rate: Double = 0.0
    var description: String = ""
    var cover: String = ""
    var detail: String = ""

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "product_id")
    val specifications: MutableSet<Specification> = HashSet()
}

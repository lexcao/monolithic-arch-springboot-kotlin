package io.lexcao.bookstore.application.payment.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.lexcao.bookstore.domain.warehouse.Product
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * 支付结算单模型
 */
data class Settlement(
    @field:Size(min = 1, message = "结算单中缺少商品清单")
    val items: Collection<Item>,
    val purchase: Purchase
) {
    /**
     * 购物清单中的商品信息
     * 基于安全原因（避免篡改价格），该信息不会取客户端的，需在服务端根据商品ID再查询出来
     */
    @Transient
    @JsonIgnore
    lateinit var productMap: Map<Int, Product>

    /**
     * 结算单中要购买的商品
     */
    data class Item(
        @field:Min(value = 1, message = "结算单中商品数量至少为一件")
        val amount: Int = 0,

        @field:JsonProperty("id")
        @field:Min(value = 1, message = "结算单中必须有明确的商品信息")
        val productId: Int = 0,
    )

    /**
     * 结算单中的配送信息
     */
    data class Purchase(
        val delivery: Boolean = true,
        @field:NotEmpty(message = "配送信息中缺少支付方式")
        val pay: String = "",
        @field:NotEmpty(message = "配送信息中缺少收件人姓名")
        val name: String = "",
        @field:NotEmpty(message = "配送信息中缺少收件人电话")
        val telephone: String = "",
        @field:NotEmpty(message = "配送信息中缺少收件地址")
        val location: String = "",
    )
}

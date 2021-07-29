package io.lexcao.bookstore.domain.payment

import io.lexcao.bookstore.domain.BaseEntity
import javax.persistence.Entity

/**
 * 商品库存
 */
@Entity
data class Stockpile(
    var amount: Int = 0,
    var frozen: Int = 0
) : BaseEntity() {

    fun frozen(number: Int) {
        amount -= number
        frozen += number
    }

    fun thawed(number: Int) {
        frozen(-1 * number)
    }

    fun decrease(number: Int) {
        frozen -= number
    }

    fun increase(number: Int) {
        amount += number
    }
}

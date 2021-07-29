package io.lexcao.bookstore.domain.payment

import io.lexcao.bookstore.domain.account.Account
import mu.KLogging
import org.springframework.stereotype.Service

/**
 * 用户钱包的领域服务
 *
 * 由于本工程中冻结、解冻款项的方法是为了在微服务中演示TCC事务所准备的，单体服务中由于与本地事务一同提交，无需用到
 */
@Service
class WalletService(
    val repository: WalletRepository
) {

    companion object : KLogging()

    private fun findOrCreate(accountId: Int) = repository.findByAccountId(accountId)
        ?: Wallet()
            .apply { account = Account(id = accountId) }
            .let { repository.save(it) }

    /**
     * 账户资金减少
     */
    fun decrease(accountId: Int, amount: Double) {
        val wallet = findOrCreate(accountId)

        if (wallet.money < amount) {
            throw RuntimeException("用户余额不足以支付，请先充值")
        }

        wallet.money -= amount
        repository.save(wallet)
        logger.info("支付成功。用户余额：{}，本次消费：{}", wallet.money, amount)
    }

    /**
     * 账户资金增加（演示程序，没有做充值入口，实际这个方法无用）
     */
    fun increase(accountId: Int, amount: Double) {
        TODO("该方法是为TCC事务准备的，在单体架构中不需要实现")
    }

    /**
     * 账户资金冻结
     * 从正常资金中移动指定数量至冻结状态
     */
    fun frozen(accountId: Int, amount: Double) {
        TODO("该方法是为TCC事务准备的，在单体架构中不需要实现")
    }

    /**
     * 账户资金解冻
     * 从冻结资金中移动指定数量至正常状态
     */
    fun thawed(accountId: Int?, amount: Double?) {
        TODO("该方法是为TCC事务准备的，在单体架构中不需要实现")
    }
}

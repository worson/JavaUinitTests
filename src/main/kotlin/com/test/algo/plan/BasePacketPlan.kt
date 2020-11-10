package com.test.algo.plan

/**
 *
 * 我们有一个背包，背包总的承载重量是 Wkg。现在我们有 n 个物品，每个物品的重量不等，并且不可分割。我们现在期望选择几件物品，装载到背包中。在不超过背包所能装载重量的前提下，如何让背包中物品的总重量最大？
 * @author worson  11.10 2020
 */
abstract class BasePacketPlan {
    /**
     * 分析：对于每个物品来说，都有两种选择，装进背包或者不装进背包。对于 n 个物品来说，总的装法就有 2^n 种，去掉总重量超过 Wkg 的，从剩下的装法中选择总重量最接近 Wkg 的。不过，我们如何才能不重复地穷举出这 2^n 种装法呢？
     */
    /**
     * 每个物品的重量
     */
    protected var ITEMS_WEIGHT = intArrayOf(2, 2, 4)

    /**
     * 背包的总容量
     */
    protected var MAX_WEIGHT = 4

    /**
     * 正确的结果值
     */
    var EXPECT_MAX_WEIGHT = 4

    /**
     * 背包最终最多装下了多重的物品
     */
    protected var maxW = Int.MIN_VALUE

    /**
     * 复杂度的次数
     */
    var counter = 0
        private set

    abstract fun maxWeight(): Int

    protected fun reset(){
        maxW = Int.MIN_VALUE
        counter=0
    }

    protected fun count(){
        counter=counter+1
    }
}
package com.test.algo.plan

import com.langogo.lib.log.L

/**
 * 回溯算法的核心在于枚举所有的可能，复杂是2^N
 * @author worson  11.10 2020
 */
internal class StandartBackPacketPlan : BasePacketPlan() {
    val  TAG = "StandartBackPacketPlan"
    override fun maxWeight(): Int {
        reset()
        findMaxWeight(0,ITEMS_WEIGHT,ITEMS_WEIGHT.size,0)
        return maxW
    }

    /**
     * @param ti 现在正在判定的序号
     * @param itemsWeights 每个当前物品
     * @param n
     * @param cWeight 目前的总重量
     */
    fun findMaxWeight(ti: Int, itemsWeights: IntArray, n: Int, cWeight: Int) {
        //结束条件，不然就会溢出啦
        if (ti == n && cWeight<=MAX_WEIGHT) {
            if (cWeight > maxW) {
                maxW = cWeight
            }
            return
        }
        L.i(TAG, "findMaxWeight: ti=$ti,cWeight=${cWeight},item=${itemsWeights[ti]}")
        count()
        if (cWeight>MAX_WEIGHT){
            return
        }
        //递归条件，要让轮子转起来

        //不选择当前物品的状态
        findMaxWeight(ti + 1, itemsWeights, n, cWeight)

        //选择当前物品的状态
        if (itemsWeights[ti] + cWeight <= MAX_WEIGHT) {
            //只有不大包承重的场景下才加入，大于就没有意义了
            findMaxWeight(ti + 1, itemsWeights, n, cWeight + itemsWeights[ti])
        }
    }
}
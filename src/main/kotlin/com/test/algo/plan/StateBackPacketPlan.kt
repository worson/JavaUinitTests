package com.test.algo.plan

import com.langogo.lib.log.L

/**
 * 记录重复路径，防止再走
 * @author worson  11.10 2020
 */
internal class StateBackPacketPlan : BasePacketPlan() {
    val  TAG = "StateBackPacketPlan"

    override fun maxWeight(): Int {
        reset()
        val states=Array<Array<Boolean>>(ITEMS_WEIGHT.size){
            Array<Boolean>(MAX_WEIGHT+1){false}
        }
        states[0][0]=false
        findMaxWeight(0,ITEMS_WEIGHT,ITEMS_WEIGHT.size,0,states)
        return maxW
    }

    /**
     * @param ti 现在正在判定的序号
     * @param itemsWeights 每个当前物品
     * @param n
     * @param cWeight 目前的总重量
     */
    fun findMaxWeight(ti: Int, itemsWeights: IntArray, n: Int, cWeight: Int,states:Array<Array<Boolean>>) {

        //结束条件，不然就会溢出啦
        if (ti == n) {
            if (cWeight > maxW) {
                maxW = cWeight
            }
            return
        }
        //如果值已经计算，直接返回
        if (states[ti][cWeight]){
            return
        }
        L.i(TAG, "findMaxWeight: ti=$ti,cWeight=${cWeight},item=${itemsWeights[ti]}")
        //递归条件，要让轮子转起来
        count()
        //不选择当前物品的状态
        findMaxWeight(ti + 1, itemsWeights, n, cWeight,states)

        //选择当前物品的状态
        val nextWeight=itemsWeights[ti] + cWeight
        if (nextWeight <= MAX_WEIGHT) {
            //只有不大包承重的场景下才加入，大于就没有意义了
            states[ti][nextWeight]=true
            findMaxWeight(ti + 1, itemsWeights, n, nextWeight,states)
        }
    }
}
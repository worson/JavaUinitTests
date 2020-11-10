package com.test.algo.plan

import com.langogo.lib.log.L
import com.langogo.lib.log.SimpleLog
import com.test.libtest.log.LogUnitTest
import org.junit.Assert
import org.junit.Test

/**
 *
 * @author worson  11.10 2020
 */
class PacketPlanTest {
    val  TAG = "PacketPlanTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    @Test
    fun testStandartBackPacketPlan(){
        getPacketResult(StandartBackPacketPlan())
    }

    @Test
    fun testStateBackPacketPlan(){
        getPacketResult(StateBackPacketPlan())
    }

    fun getPacketResult(plan:BasePacketPlan) :Int{
        val result=plan.maxWeight()
        L.i(TAG, "getPacketResult: class=${plan.javaClass.simpleName},result=${result},count=${plan.counter}")
        Assert.assertEquals(plan.EXPECT_MAX_WEIGHT,result)
        return result
    }
}
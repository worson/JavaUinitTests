package com.test.kotlin.basic

import org.junit.Test
import kotlin.system.measureTimeMillis

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class TestLamdaPerformance {

    @Test
    fun testPerformance() {
        val times=100_000_000
        val msg="hello"
        val stdCost= measureTimeMillis {
            for (i in 0 .. times){
                stdPrint(msg)
            }
        }

        val lamdaCost= measureTimeMillis {
            for (i in 0 .. times){
                lamdaPrint{ msg}
            }
        }
        println("stdCost=$stdCost,lamdaCost=$lamdaCost")

    }

    fun stdPrint(msg:String){

    }

    fun lamdaPrint(msg: () -> String){

    }
}
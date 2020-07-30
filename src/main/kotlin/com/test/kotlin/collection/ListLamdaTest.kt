package com.test.kotlin.collection

import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  07.30 2020
 */
class ListLamdaTest {

    @Test
    fun testFold(){
        val list = listOf(1,2,3)
        val result=list.fold(0){
            a,f ->
            a+f
        }
        println("testFold:$result")
    }
}
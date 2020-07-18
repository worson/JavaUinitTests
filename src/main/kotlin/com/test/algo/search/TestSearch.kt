package com.test.algo.search

import org.junit.Assert
import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  07.18 2020
 */
class TestSearch {

    @Test
    fun testBinerySearch(){
        val arr = arrayOf(1,3,5,6)
        val index=BinarySearch.search(arr,3)
        Assert.assertEquals(0,BinarySearch.search(arr,1))
        Assert.assertEquals(1,BinarySearch.search(arr,3))
        Assert.assertEquals(2,BinarySearch.search(arr,5))

        println("testBinerySearch ${2.compareTo(1)}")
        println("testBinerySearch $index")
    }
}
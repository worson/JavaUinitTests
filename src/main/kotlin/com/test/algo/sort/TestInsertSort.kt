package com.test.algo.sort

import org.junit.Test
import kotlin.math.max

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
class TestInsertSort {

    @Test
    fun intertSortCost(){
        SortUtil.batchTest(max=10,sortB = InsertSort::insertSort)
    }

    @Test
    fun insertCpBubble(){
        SortUtil.batchTest(max=10000,sortA = InsertSort::insertSort,sortB = BubbleSort::bubbleSort)
    }
}
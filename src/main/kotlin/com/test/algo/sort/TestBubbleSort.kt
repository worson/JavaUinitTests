package com.test.algo.sort

import com.test.algo.sort.BubbleSort.bubbleSort
import com.test.algo.sort.SortUtil.arrayGenerate
import com.test.algo.sort.SortUtil.batchTest
import com.test.algo.sort.SortUtil.sortCompare
import com.test.algo.sort.StdSort.stdSort
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import kotlin.random.Random

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
class  TestBubbleSort {
    val sourceData= arrayOf(1,5,3,2)
    val tartgetData= arrayOf(1,2,3,5)

    @Test
    fun stdLibSort(){
        assertArrayEquals("same array ",tartgetData, sourceData.copyOf().apply {
            sort()
        })
    }

    @Test
    fun bubbleSortTests(){
        val src=sourceData.copyOf()
        bubbleSort(src)
        assertArrayEquals(tartgetData, src)
    }

    @Test
    fun bubbleSortCost(){
        batchTest(sortB = ::bubbleSort)
    }


    /**
     *
    sortCompare#sort size =50, diff=10,   A cost =10,A percost=200,   B cost=0,B percost=0
    sortCompare#sort size =250, diff=-3,   A cost =0,A percost=0,   B cost=3,B percost=12
    sortCompare#sort size =1250, diff=-11,   A cost =1,A percost=0,   B cost=12,B percost=9
    sortCompare#sort size =6250, diff=-119,   A cost =6,A percost=0,   B cost=125,B percost=20
    sortCompare#sort size =31250, diff=-3008,   A cost =11,A percost=0,   B cost=3019,B percost=96
     */


}
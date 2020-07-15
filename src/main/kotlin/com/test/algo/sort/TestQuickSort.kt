package com.test.algo.sort

import com.test.algo.sort.MergeSort.mergeSort
import org.junit.Test
import kotlin.math.max

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
class TestQuickSort {

    /**
     *
    sortCompare#sort size =10, diff=12,   A cost =12,A percost=1200,   B cost=0,B percost=0
    sortCompare#sort size =50, diff=1,   A cost =1,A percost=20,   B cost=0,B percost=0
    sortCompare#sort size =250, diff=-1,   A cost =0,A percost=0,   B cost=1,B percost=4
    sortCompare#sort size =1250, diff=1,   A cost =1,A percost=0,   B cost=0,B percost=0
    sortCompare#sort size =6250, diff=3,   A cost =5,A percost=0,   B cost=2,B percost=0
    sortCompare#sort size =31250, diff=8,   A cost =13,A percost=0,   B cost=5,B percost=0
    sortCompare#sort size =156250, diff=150,   A cost =176,A percost=1,   B cost=26,B percost=0
    sortCompare#sort size =781250, diff=101,   A cost =268,A percost=0,   B cost=167,B percost=0
    sortCompare#sort size =3906250, diff=373,   A cost =1412,A percost=0,   B cost=1039,B percost=0
     */
    @Test
    fun mergeSortCost(){
        SortUtil.batchTest(max=1_000_000,sortB = QuickSort::quickSort)
    }

    /**
    sortCompare#sort size =10, diff=0,   A cost =0,A percost=0,   B cost=0,B percost=0
    sortCompare#sort size =50, diff=0,   A cost =0,A percost=0,   B cost=0,B percost=0
    sortCompare#sort size =250, diff=-3,   A cost =0,A percost=0,   B cost=3,B percost=12
    sortCompare#sort size =1250, diff=-14,   A cost =0,A percost=0,   B cost=14,B percost=11
    sortCompare#sort size =6250, diff=-123,   A cost =2,A percost=0,   B cost=125,B percost=20
    sortCompare#sort size =31250, diff=-2752,   A cost =4,A percost=0,   B cost=2756,B percost=88
    sortCompare#sort size =156250, diff=-73521,   A cost =27,A percost=0,   B cost=73548,B percost=470
     */
    @Test
    fun insertCpBubble(){
        SortUtil.batchTest(max=1_000_000,sortA = MergeSort::mergeSort,sortB = QuickSort::quickSort)
    }
}
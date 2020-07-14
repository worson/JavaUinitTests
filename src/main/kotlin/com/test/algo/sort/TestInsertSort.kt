package com.test.algo.sort

import org.junit.Test
import kotlin.math.max

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
class TestInsertSort {

    /**
     *
    sortCompare#sort size =10, diff=12,   A cost =13,A percost=1300,   B cost=1,B percost=100
    sortCompare#sort size =50, diff=0,   A cost =0,A percost=0,   B cost=0,B percost=0
    sortCompare#sort size =250, diff=-2,   A cost =0,A percost=0,   B cost=2,B percost=8
    sortCompare#sort size =1250, diff=-14,   A cost =2,A percost=1,   B cost=16,B percost=12
    sortCompare#sort size =6250, diff=-17,   A cost =10,A percost=1,   B cost=27,B percost=4
    sortCompare#sort size =31250, diff=-1102,   A cost =14,A percost=0,   B cost=1116,B percost=35
     */
    @Test
    fun intertSortCost(){
        SortUtil.batchTest(max=100000,sortB = InsertSort::insertSort)
    }

    /**
    sortCompare#sort size =10, diff=1,   A cost =1,A percost=100,   B cost=0,B percost=0
    sortCompare#sort size =50, diff=0,   A cost =0,A percost=0,   B cost=0,B percost=0
    sortCompare#sort size =250, diff=-4,   A cost =1,A percost=4,   B cost=5,B percost=20
    sortCompare#sort size =1250, diff=-4,   A cost =22,A percost=17,   B cost=26,B percost=20
    sortCompare#sort size =6250, diff=-79,   A cost =49,A percost=7,   B cost=128,B percost=20
    sortCompare#sort size =31250, diff=-2381,   A cost =943,A percost=30,   B cost=3324,B percost=106
     */
    @Test
    fun insertCpBubble(){
        SortUtil.batchTest(max=1000000,sortA = InsertSort::insertSort,sortB = BubbleSort::bubbleSort)
    }
}
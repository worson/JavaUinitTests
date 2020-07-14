package com.test.algo.sort

import com.test.algo.sort.SortUtil.sortCompare
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import sun.security.util.Length
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
object  BubbleSort {

    fun bubbleSort(src:Array<Int>){
        val length=src.size
        for (i in 0 .. length-1){
            for (j in i .. length-1){
                if (src[j] < src[i]){
                    val t=src[j]
                    src[j]=src[i]
                    src[i]=t
                }
            }
        }
    }


}
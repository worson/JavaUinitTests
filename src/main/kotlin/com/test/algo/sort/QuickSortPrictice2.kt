package com.test.algo.sort

import org.junit.Assert
import java.util.*
import kotlin.math.max

/**
 * 说明:快速排序
 * @author wangshengxing  07.14 2020
 */
object QuickSort2 {


    fun quickSort(src: Array<Int>) {
        quickSort(src, 0, src.size - 1)
    }


    fun quickSort(src: Array<Int>, p: Int, r: Int) {
        val dataInfo = "[${src.copyOfRange(p, r + 1).joinToString()}]"
        val q = partition(src, p, r)
        println("quickSort:p=$p,r=$r,q=$q,arr=${dataInfo}")
        if ((q - 1 > p)) {
            quickSort(src, p, q - 1)
        }
        if ((q + 1 < r)) {
            quickSort(src, q + 1, r)
        }
    }

    /**
     * 核心原理是找到一个切点，比切点小的数据乱序放左边，比切点大的数据乱序放右边，返回最终切点的下标
     */
    fun partition(src: Array<Int>, p: Int, r: Int): Int {
        var i = r - 1
        //预设为切点
        var q = r
        //拟好最大值
        var v = src[r]
        do {
            //大于或等于右边值，放到切点右边去
            if (src[i] >= v) {
                val t = src[i]
                //先把左边要移动到右边的空位整体右左挤掉
                for (j in i..(q - 1)) {
                    src[j] = src[j + 1]
                }
                //把那个挤出来的大值放到原来切点的位置
                src[q] = t
                q = q - 1
            }
            i--
        } while (i >= p)
        println("partition:p=$p,r=$r,q=$q")
        return q
    }

}

fun main() {
    val sourceData = arrayOf(95, 70, 15, 38, 69, 95, 97, 14, 22, 33)//, 22, 33
//    val sourceData= arrayOf(95,70, 15)//, 22, 33
//    val sourceData= SortUtil.arrayGenerate(10)
    println("origin:${Arrays.toString(sourceData)}")
    QuickSort2.quickSort(sourceData)
    println("result:${Arrays.toString(sourceData)}")
    val stdResult = sourceData.copyOf().apply { sort() }
    println("std result:${Arrays.toString(stdResult)}")
    Assert.assertArrayEquals(stdResult, sourceData)
}
package com.test.algo.sort

import org.junit.Assert
import java.util.*

/**
 * 说明:快速排序
 * @author wangshengxing  07.14 2020
 */
object QuickSort {


    fun quickSort(src:Array<Int>){
        quickSort(src,0,src.size-1)
    }


    fun quickSort( src:Array<Int>, p:Int, r:Int){
        if (p>=r ) return
        val q=partition(src,p,r)
//        println("quickSort:p=${p},q=${q},r=${r}")
        if (p!=q-1){
            quickSort(src,p,q-1)
        }
        if (q+1!=r){
            quickSort(src,q+1,r)
        }

    }
    /**
     * 核心原理是找到一个切点，比切点小的数据乱序放左边，比切点大的数据乱序放右边，返回最终切点的下标
     */
    fun partition(src:Array<Int>, p:Int,r:Int):Int{
        //最终分切点的下标
        var o=r
        var c=src[o]
        var i=r-1
        var j=0
        //核心目标是找到比c小的值放在左边，比c的在右边
        while (i>=p){
            //比c大，移到c右边，可以通过交换两者的位置
            //示例：95,70, 15, 38, 69 ，中的69作为比较点
            if(src[i]>c){
                //左边的数15, 38,要往前挤，将70与位置挤到当前69的位置
                val t = src[i]
                //拷贝到切前点前一个点
                val e=o-1
                j=i+1
                while (j<=e){
                    src[j-1]=src[j]
                    j++
                }
                src[o-1]=c
                //把当前那个大于切点值放到最右,更新切点的位置
                src[o]=t
                o=o-1

            }
            //比c小，继续保持在c左边
            i--
        }
        return o
    }

}

fun main() {
//    val sourceData= arrayOf(95,70, 15, 38, 69, 95, 97, 14,22, 33)//, 22, 33
    val sourceData= SortUtil.arrayGenerate(10)
    println("origin:${Arrays.toString(sourceData)}")
    QuickSort.quickSort(sourceData)
    println("result:${Arrays.toString(sourceData)}")
    val stdResult=sourceData.copyOf().apply { sort() }
    println("std result:${Arrays.toString(stdResult)}")
    Assert.assertArrayEquals(stdResult,sourceData)
}
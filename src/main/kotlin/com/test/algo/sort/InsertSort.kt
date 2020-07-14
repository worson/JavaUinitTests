package com.test.algo.sort

import com.test.algo.sort.InsertSort.insertSort
import java.util.*

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
object InsertSort {

    /**
     * 涉及到数据拷贝，暂时未实现
     */
    fun insertSort(src:Array<Int>){
        val length=src.size
        for (i in 0 .. length-2){
            val j=i+1
            println(j)
            //新的数据比已排序的最后一个要小，那么就要刚好大的数进行插入，然后剩余的数据向右移动
            if (src[j] < src[i]){
                var l=0
                do {
                    if (src[l] < src[j]){
                        arraySwapMove(src,j,l)
                        break
                    }
                    l++
                }while (l<=j-1)
                //一直没有找到说明是最好一个
//                arraySwapMove(src,j,0)

            }
        }
    }

    fun arraySwapMove(src:Array<Int>,targetIndex:Int,start:Int){
        println("arraySwapMove: targetIndex=${targetIndex},start=${start}")
        val target=src[targetIndex]
        for (i in targetIndex downTo start+1){
            src[i]=src[i-1]
        }
        src[start]=target
        println("result:${Arrays.toString(src)}")
    }
}

fun main() {
    val sourceData= arrayOf(1,5,3,2)
    println("origin:${Arrays.toString(sourceData)}")
    insertSort(sourceData)
    println("result:${Arrays.toString(sourceData)}")
}
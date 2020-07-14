package com.test.algo.sort

import com.test.algo.sort.MergeSort.mergeSort
import org.junit.Assert
import java.util.*

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
object MergeSort {

    /**
     * 涉及到数据拷贝，暂时未实现
     */
    fun mergeSort(src:Array<Int>){

        mergeSort(src,src.copyOf(),0,src.size-1)
    }

    fun mergeSort(result:Array<Int>,src:Array<Int>,p:Int ,q:Int){
        val r=(p+q)/2
        if (p>=q || r==p) return
//        println("mergeSort:p=${p},r=${r},q=${q}")
        mergeSort(result,src,p,r)
        mergeSort(result,src,r,q)
        mergeArray(result,src,p,r,q)

    }
    fun mergeArray(result:Array<Int>,src:Array<Int>,p:Int ,r:Int,q:Int){
        val total=(r-p)+(q-r)
        var c=0
        println("mergeArray:p=${p},r=${r},q=${q},c=${total}")
        var i=p
        var j=r
        do {
            c++
            if (src[])

            if (i<r){

            }else if (j<q){

            }
        }while (c>total)
    }

}

fun main() {
    val sourceData= arrayOf(70, 95, 15, 38, 74, 95, 97, 14, 22, 33)
//    val sourceData= SortUtil.arrayGenerate(10)
    println("origin:${Arrays.toString(sourceData)}")
    mergeSort(sourceData)
    println("result:${Arrays.toString(sourceData)}")
    val stdResult=sourceData.copyOf().apply { sort() }
    println("std result:${Arrays.toString(stdResult)}")
    Assert.assertArrayEquals(sourceData, stdResult)
}
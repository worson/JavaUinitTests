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

    fun mergeSort(result:Array<Int>, src:Array<Int>, p:Int, r:Int){
        val q=(p+r+1)/2
        if (p>=r || p==q ) return
//        println("mergeSort:p=${p},q=${q},r=${r}")
        mergeSort(result,src,p,q-1)
        mergeSort(result,src,q,r)
        mergeArray(result,src,p,q,r)

    }
    fun mergeArray(result:Array<Int>,src:Array<Int>,p:Int ,q:Int,r:Int){
        val total=(r-p+1)
        val g=q-1
        var c=0
//        println("mergeArray:(${p},${g}),(${q},${r}),total=${total}")
        var i=p
        var j=q
        while (i<=(g) && j<=r){
            if (src[i]<=src[j]){
                result[c]=src[i]
                i++
            }else{
                result[c]=src[j]
                j++
            }
            c++
        }
        val start=if (i<=(g)) i else if (j<=r)  j else Int.MAX_VALUE
        val end=if (i<=(g)) g  else if (j<=r) r else -1
//        println("mergeArray:start=$start,end=$end,src result -> ${src.copyOfRange(p,r+1).asList()}")

        var index=start
        while (index<=end){
            result[c]=src[index]
            index++
            c++
        }

        System.arraycopy(result,0,src,p,total)
//        println("mergeArray:result -> ${result.copyOf(total).asList()}")
    }

}

fun main() {
//    val sourceData= arrayOf(95,70,  15, 38, 74, 95, 97, 14, 22, 33)//, 22, 33
    val sourceData= SortUtil.arrayGenerate(10)
    println("origin:${Arrays.toString(sourceData)}")
    mergeSort(sourceData)
    println("result:${Arrays.toString(sourceData)}")
    val stdResult=sourceData.copyOf().apply { sort() }
    println("std result:${Arrays.toString(stdResult)}")
    Assert.assertArrayEquals(sourceData, stdResult)
}
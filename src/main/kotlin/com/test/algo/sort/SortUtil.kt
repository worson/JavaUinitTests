package com.test.algo.sort

import org.junit.Assert
import java.util.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */

object SortUtil{

    fun arrayGenerate(size:Int=1000):Array<Int>{
        val src=Array<Int>(size,{0})
        for (i in 0 .. size-1){
            src[i]= Random.nextInt()
        }
        return src
    }

    fun sortCompare(arr:Array<Int>,sortA: (Array<Int>) -> Unit,sortB: (Array<Int>) -> Unit){
        val arrA = arr.copyOf()
        val arrB = arr.copyOf()
        val costA = measureTimeMillis {
            sortA(arrA)
        }
        val costB = measureTimeMillis {
            sortB(arrB)
        }
        val size=arr.size

//        println("sortCompare#sort A Result = ${Arrays.toString(arrA)}")
//        println("sortCompare#sort B Result = ${Arrays.toString(arrB)}")

        Assert.assertArrayEquals(arrA, arrB)

        println("sortCompare#sort size =${size}, diff=${costA-costB},   A cost =${costA},A percost=${costA*1000/size},   B cost=${costB},B percost=${costB*1000/size}")
    }

    fun batchTest(start:Int=10,factor:Int=5,max:Int=1_00_000,sortA: (Array<Int>) -> Unit=StdSort::stdSort,sortB: (Array<Int>) -> Unit){
        var size=start
        for (i in 1 .. Int.MAX_VALUE){
            if (size<=max) {
                val src = arrayGenerate(size)
                sortCompare(src, sortA, sortB)
            }else{
                break
            }
            size *=factor
        }

    }
}
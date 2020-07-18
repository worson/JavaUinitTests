package com.test.algo.search

/**
 * 说明:
 * @author wangshengxing  07.18 2020
 */
object BinarySearch {

    fun <T:Comparable<T>> search(arr:Array<T>,value:T):Int{
        var l = 0
        var r = arr.size-1
        var m = (l+r)/2
        while (l<=r){
            val t=arr[m].compareTo(value)
            if (t==0){
                return m
            }else if (t>0){//太大了往左边找
                r=m-1
            }else{
                l=m+1
            }
            m = (l+r)/2
        }
        return -1
    }
}
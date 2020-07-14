package com.test.kotlin.coroutine.ch03

import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun notSuspend() = suspendCoroutine<Int> { continuation ->
//    continuation.resume(100)
    thread {
        Thread.sleep(100)
        continuation.resume(100)
    }

}

suspend fun main(){
    val result=notSuspend()
    println("notSuspend end ${result}")
}
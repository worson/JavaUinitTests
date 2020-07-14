package com.bennyhuo.kotlin.coroutine.ch03

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine

fun main() {
    val continuation = suspend {
        println("In Coroutine.")
        5
    }.createCoroutine(object : Continuation<Int> {
        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End: $result")
        }

        override val context = EmptyCoroutineContext
    })
//    continuation.resumeWith(Result.success(Unit))
    continuation.resumeWith(Result.failure(Exception("test")))

    val suspendVal=suspend {
        println("In Coroutine.")
        5
    }
    println(continuation::javaClass.get())
    println(suspendVal::javaClass.get())
    GlobalScope.launch(Dispatchers.Main) {
        kotlinx.coroutines.delay(10)
    }
}
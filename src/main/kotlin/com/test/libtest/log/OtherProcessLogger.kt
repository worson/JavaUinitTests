package com.test.libtest.log

import com.langogo.lib.log.L
import java.io.File

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */

fun main() {
    val  TAG = "OtherProcessLogger"
    L.init(debug = true, stackDepth = 6,logPath = null)
    var cnt=2
    while ((cnt--)>0){
        L.i(TAG, "main: send log to main ${cnt} ")
        Thread.sleep(500)
    }
//    Thread.sleep(30000)
    L.flush(0)
    Thread.sleep(3000)
    println("main:exit ")
}

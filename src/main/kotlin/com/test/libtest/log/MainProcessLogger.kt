package com.test.libtest.log

import com.langogo.lib.log.L
import java.io.File

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */

fun main() {
    val  TAG = "MainProcessLogger"
    val logDir=File("src/main/res/test/log/main")
    L.init(debug = true, stackDepth = 6,logPath = logDir)
    L.i(TAG, "main: main log started")
    while (true){
        Thread.sleep(1000)
    }
}

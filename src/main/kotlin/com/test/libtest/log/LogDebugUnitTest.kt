package com.test.libtest.log

import com.langogo.lib.log.internal.LogDebug
import com.langogo.lib.log.internal.StackTraceUtil

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */
private val  TAG = "LogDebugUnitTest"
private val L = LogDebug.debugLogger

fun main() {
    L.i(TAG, "main: start")
    L.i(TAG, "main: stack info ${StackTraceUtil.getRuntimeCaller(2)}")
}

class LogDebugUnitTest {


}
package com.test.libtest.log

import com.langogo.lib.log.L
import com.langogo.lib.log.SimpleLog
import com.langogo.lib.log.internal.StackTraceUtil
import org.junit.Assert
import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */
class LogStackInfoUnitTest {
    val  TAG = "LogStackInfoUnitTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    @Test
    fun showStackInfo(){
        //指定在23行
        val info=StackTraceUtil.getRuntimeCaller(2)
        L.i(TAG, "showStackInfo: ${info}")
        L.i(TAG, "showStackInfo: ${StackTraceUtil.getCurrentStackInfo()}")
        Assert.assertEquals(true,info.contains("LogStackInfoUnitTest.kt:23"))
    }
}
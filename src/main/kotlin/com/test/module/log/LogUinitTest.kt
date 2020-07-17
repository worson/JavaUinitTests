package com.test.module.log

import com.langogo.transcribe.comm.L
import com.langogo.transcribe.comm.log.LogConfiguration
import com.langogo.transcribe.comm.log.internal.StackTraceUtil
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import com.langogo.transcribe.comm.log.printer.FilePrinter
import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class LogUinitTest {
    val TAG="LogUinitTest"

    init {
        val logDir="src/main/res/test/log/dev"
        L.init(LogConfiguration.Builder()
            .addPrinter(ConsolePrinter())
            .addPrinter(FilePrinter.Builder(logDir).build())
            .build())
    }

    @Test
    fun testFileLog(){
        for(i in 0 .. 100){
            L.d("hello $i ")
        }
    }

    @Test
    fun testLog(){
        println("   ${StackTraceUtil.getRuntimeCaller(2)}")
        L.i(TAG, { "testLog: ${StackTraceUtil.getStackTraceString(Throwable())}" })
        L.i(TAG, { "testLog: ${StackTraceUtil.getRuntimeCaller(6)}" })
    }
}
package com.test.module.log

import com.langogo.transcribe.comm.L
import com.langogo.transcribe.comm.log.LogConfiguration
import com.langogo.transcribe.comm.log.internal.StackTraceUtil
import com.langogo.transcribe.comm.log.internal.SystemCompat
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import com.langogo.transcribe.comm.log.printer.FilePrinter
import com.langogo.transcribe.comm.log.report.ZipLogHandler
import org.junit.Test
import java.io.File

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class LogUinitTest {
    val TAG="LogUinitTest"

    init {
        val logDir="src/main/res/test/log/dev/log"
        L.init(LogConfiguration.Builder()
//            .addPrinter(ConsolePrinter())
            .addPrinter(FilePrinter.Builder(logDir)
                .logHandler(ZipLogHandler("src/main/res/test/log/dev/log/backup"))
                .build())
            .build())
    }

    @Test
    fun testFileSort(){
        val arr = arrayOf(3,1,5)
        arr.sortBy { -it }
        println("list = ${arr.toList()}")
    }

    @Test
    fun testFileLog(){

        for(i in 0 .. 100){
            L.d("hello $i ")
        }
        Thread.sleep(2000)
    }

    @Test
    fun testLog(){
        println("   ${StackTraceUtil.getRuntimeCaller(2)}")
        L.i(TAG, { "testLog: ${StackTraceUtil.getStackTraceString(Throwable())}" })
        L.i(TAG, { "testLog: ${StackTraceUtil.getRuntimeCaller(6)}" })
    }
}
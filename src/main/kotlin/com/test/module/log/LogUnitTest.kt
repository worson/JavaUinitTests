package com.test.module.log

import com.langogo.transcribe.comm.L
import com.langogo.transcribe.comm.log.LogConfiguration
import com.langogo.transcribe.comm.log.LogLevel
import com.langogo.transcribe.comm.log.internal.Platform
import com.langogo.transcribe.comm.log.internal.StackTraceUtil
import com.langogo.transcribe.comm.log.printer.AndroidPrinter
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import com.langogo.transcribe.comm.log.printer.FilePrinter
import com.langogo.transcribe.comm.log.printer.file.handler.ZipLogHandler
import com.langogo.transcribe.comm.log.printer.file.reporter.LogFileReporter
import org.junit.Test
import java.io.File

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class LogUnitTest {
    val TAG="LogUinitTest"
    val logDir="src/main/res/test/log/dev/log"
    val backDir="src/main/res/test/log/dev/log/backup"

    init {

        L.init(LogConfiguration.Builder()
            .tag("Langogo")
            .logLevel(LogLevel.ALL)
            .threadInfo(true)
            .traceInfo(true,6)
            .addPrinter(Platform.get().defaultPrinter())
            .addPrinter(
                FilePrinter.Builder(logDir)
                .logHandler(ZipLogHandler(backDir, limitSize = 100*1024*1024,reporter=LogFileReporter()))
                .build())
            .build())

//        L.init(false, File("src/main/res/test/log/dev/log"))
    }
    @Test
    fun testPrint(){
        L.d(TAG,"testPrint")
    }


    @Test
    fun testFileSort(){
        val arr = arrayOf(3,1,5)
        arr.sortBy { -it }
        L.d(TAG,"list = ${arr.toList()}")
        L.flush()
        Thread.sleep(500)
    }

    @Test
    fun testFileSplitSort(){
        for (i in 0 .. 100000){
            L.d(TAG, { "testFileSplitSort: ${i}" })
        }
        L.flush()
        Thread.sleep(500)
    }

    @Test
    fun testFileLog(){
        for(i in 0 .. 100){
            L.d(TAG,"hello $i ")
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
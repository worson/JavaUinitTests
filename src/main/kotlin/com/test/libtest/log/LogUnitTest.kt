package com.test.libtest.log

import com.langogo.lib.log.L
import com.langogo.lib.log.LogConfiguration
import com.langogo.lib.log.LogLevel
import com.langogo.lib.log.internal.LogDebug
import com.langogo.lib.log.internal.Platform
import com.langogo.lib.log.internal.StackTraceUtil
import com.langogo.lib.log.printer.FilePrinter
import com.langogo.lib.log.printer.file.handler.ZipLogHandler
import com.langogo.lib.log.printer.file.reporter.LogFileReporter
import org.junit.Test
import java.io.File
import java.lang.management.ManagementFactory

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class LogUnitTest {
    val TAG="LogUinitTest"
    val logDir="src/main/res/test/log/dev/log/logging"
    val backDir="src/main/res/test/log/dev/log/backup"
    val uploadDir="src/main/res/test/log/dev/log/upload"
    init {
        LogDebug.IS_DEBUG=true

        L.init(
            LogConfiguration.Builder()
            .tag("Langogo")
            .logLevel(LogLevel.ALL)
            .threadInfo(true)
            .traceInfo(true,6)
            .addPrinter(Platform.get().defaultPrinter())
            .addPrinter(
                FilePrinter.Builder(logDir)
                .logHandler(
                    ZipLogHandler(
                        backDir,
                        limitSize = 100 * 1024 * 1024,
                        password="heyan1234",
                        reporter = LogFileReporter(File(uploadDir))
                    )
                )
                .build())
            .build())

//        L.init(false, File("src/main/res/test/log/dev/log"))
    }
    @Test
    fun testPrint(){
        L.d(TAG,"testPrint")
    }

    @Test
    fun testPid(){
        val pid = ManagementFactory.getRuntimeMXBean()?.name?.split("@")?.first()
        System.out.println("当前JVM Process ID: " + pid)
    }


    @Test
    fun testFileSort(){
        val arr = arrayOf(3,1,5)
        arr.sortBy { -it }
        L.d(TAG,"list = ${arr.toList()}")
        L.flush(0)
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


    @Test
    fun testLogFlush(){
        for(i in 0 .. 100){
            L.d(TAG,"hello $i ")
        }
        L.flush()
        Thread.sleep(1000)
    }

    @Test
    fun testMultiLogFlush(){
        for (j in 0 .. 1){
            for(i in 0 .. 100){
                L.d(TAG,"hello $i ")
            }
            L.flush()
            Thread.sleep(1000)
        }
        Thread.sleep(1000)
    }
}
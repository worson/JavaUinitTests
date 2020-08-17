package com.test.libtest.log

import com.langogo.lib.log.L
import com.langogo.lib.log.LogConfiguration
import com.langogo.lib.log.LogLevel
import com.langogo.lib.log.internal.Platform
import com.langogo.lib.log.printer.FilePrinter
import com.langogo.lib.log.printer.file.handler.ZipLogHandler
import com.langogo.lib.log.printer.file.reporter.LogFileReporter
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

/**
 * 说明:
 * @author wangshengxing  08.16 2020
 */
class LogPerformanceTest {

    val MULTI_THREAD_CNT=100
    val REPEAT_TEST_CNT=30
    val TEST_PRINT_CNT=1_0000

    @Test
    fun testMeasureTool(){
        for (j in 0 .. 10) {
            val totolCost = measureTimeMillis {
                for (i in 0..1_000_000) {
                    System.currentTimeMillis()
                }
            }
            //平均每次32ns
            println("testMeasureTool: totolCost =${totolCost}")
        }
    }

    @Test
    //结果是: 4.4 us
    fun testPrintCost(){
        val list = Vector<Long>(TEST_PRINT_CNT)
        for (j in 0 .. REPEAT_TEST_CNT-1) {
            val totolCost = measureTimeMillis {
                for (i in 1 .. TEST_PRINT_CNT) {
                    println("testPrintCost")
                }
            }
            list.add(totolCost)
            println("testPrintCost: totolCost =${totolCost}")
        }
        println("testPrintCost: result=${list}, average=${list.average()}")
    }

    @Test
    //结果是: 48 us
    fun testDebugLogConsolePrinter(){
//        L.init(debug = true, stackDepth = 6,logPath = null)
        L.init(
            LogConfiguration.Builder()
                .tag("Langogo")
                .logLevel(LogLevel.ALL)
                .threadInfo(true)
                .traceInfo(true,6)
                .addPrinter(Platform.get().defaultPrinter())
                .build())
        val list = Vector<Long>(TEST_PRINT_CNT)
        for (j in 0 .. REPEAT_TEST_CNT-1) {
            val totolCost = measureTimeMillis {
                for (i in 1 .. TEST_PRINT_CNT) {
                    L.i("testPrintCost")
                }
            }
            list.add(totolCost)
            println("testPrintCost: totolCost =${totolCost}")
        }
        println("testPrintCost: result=${list}, average=${list.average()}")
    }

    @Test
    //结果是: 10 us
    fun testOnlyLogConsolePrinter(){
        L.init(
            LogConfiguration.Builder()
                .tag("Langogo")
                .logLevel(LogLevel.ALL)
                .threadInfo(false)
                .traceInfo(false,6)
                .addPrinter(Platform.get().defaultPrinter())
                .build())
        val list = Vector<Long>(TEST_PRINT_CNT)
        for (j in 0 .. REPEAT_TEST_CNT-1) {
            val totolCost = measureTimeMillis {
                for (i in 1 .. TEST_PRINT_CNT) {
                    L.i("testPrintCost")
                }
            }
            list.add(totolCost)
            println("testPrintCost: totolCost =${totolCost}")
        }
        println("testPrintCost: result=${list}, average=${list.average()}")
    }

    @Test
    //结果是: 1 us
    fun testOnlyLogFilePrinter(){
        val logDir="src/main/res/test/log/dev/log/logging"
        val backDir="src/main/res/test/log/dev/log/backup"
        val uploadDir="src/main/res/test/log/dev/log/upload"
        L.init(
            LogConfiguration.Builder()
                .tag("Langogo")
                .logLevel(LogLevel.ALL)
                .threadInfo(false)
                .traceInfo(false,6)
//                .addPrinter(Platform.get().defaultPrinter())
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
        val list = Vector<Long>(TEST_PRINT_CNT)
        val threads=ArrayList<Thread>(MULTI_THREAD_CNT)
        for (t in 1 .. MULTI_THREAD_CNT){
            val r=Thread {
                for (j in 0 .. REPEAT_TEST_CNT-1) {
                    val totolCost = measureTimeMillis {
                        for (i in 1 .. TEST_PRINT_CNT) {
                            L.i("testPrintCost")
                        }
                    }
                    list.add(totolCost)
//                    println("testPrintCost: totolCost =${totolCost}")
                }
            }
            threads.add(r)
            r.start()
        }
        for ( r in threads){
            r.join()
        }
        println("testPrintCost: result=${list}, average=${list.average()}")
    }

}
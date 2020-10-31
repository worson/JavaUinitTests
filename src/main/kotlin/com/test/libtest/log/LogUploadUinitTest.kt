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

/**
 * 说明:
 * @author wangshengxing  08.05 2020
 */
class LogUploadUinitTest {
    val TAG="LogUinitTest"
    val logDir="src/main/res/test/log/dev/log/logging"
    val backDir="src/main/res/test/log/dev/log/backup"
    val uploadDir="src/main/res/test/log/dev/log/upload"

    val reporter = S3LogUploader(File(uploadDir))
    init {

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
                                reporter=reporter
                            )
                        )
                        .build())
                .build())

    }

    @Test
    fun testFileUploader(){
        for (i in 0 .. 5){
            L.d(TAG, { "testFileSplitSort: ${i}" })
        }
        L.flush()
        L.d(TAG, { "testFileSplitSort: ${-1}" })
        Thread.sleep(500)
    }

    @Test
    fun testFileShare(){
        reporter.putInterceptorListener(S3LogUploader.FLUSH_TYPE_SHARE,object :
            S3LogUploader.LogFileInterceptorListener{
            override fun interceptFile(item: LogFileReporter.ReportItem): Boolean {
                L.i(TAG, "interceptFile: ${item}")
                return true
            }
        })
        for (i in 0 .. 5){
            L.d(TAG, { "testFileShare: ${i}" })
        }
        L.flush(S3LogUploader.FLUSH_TYPE_SHARE)
        L.d(TAG, { "testFileShare: ${-1}" })
        Thread.sleep(500)
    }
}
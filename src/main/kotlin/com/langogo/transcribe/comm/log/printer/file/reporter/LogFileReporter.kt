package com.langogo.transcribe.comm.log.printer.file.reporter

import com.langogo.transcribe.comm.log.printer.file.handler.LogFileHandler
import com.langogo.transcribe.comm.log.printer.file.handler.ZipLogHandler
import java.io.File

/**
 * 说明:文件上报
 * @author wangshengxing  07.29 2020
 */
class LogFileReporter {

    class ReportItem(val file: File){

    }

    open fun onReport(handler: LogFileHandler, item:ReportItem) {
        println("onReport: ${item.file.absolutePath} ")
    }
}
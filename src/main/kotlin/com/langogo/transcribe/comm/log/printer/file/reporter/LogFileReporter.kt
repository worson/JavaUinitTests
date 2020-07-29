package com.langogo.transcribe.comm.log.printer.file.reporter

import java.io.File

/**
 * 说明:文件上报
 * @author wangshengxing  07.29 2020
 */
class LogFileReporter {

    class ReportItem(val file:File){

    }

    open fun onReport(item:ReportItem) {

    }
}
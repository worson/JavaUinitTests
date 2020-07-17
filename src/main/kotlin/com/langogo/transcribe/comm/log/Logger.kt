package com.langogo.transcribe.comm.log

import com.langogo.transcribe.comm.log.format.DefaultFlattener
import com.langogo.transcribe.comm.log.format.Flattener
import com.langogo.transcribe.comm.log.internal.SystemCompat
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import com.langogo.transcribe.comm.log.printer.FilePrinter
import com.langogo.transcribe.comm.log.printer.Printer
import com.langogo.transcribe.comm.log.printer.PrinterSet

/**
 * 说明:日志输出总入口
 * @author wangshengxing  07.17 2020
 */
class Logger(val logConfiguration: LogConfiguration) : Printer{

    private var  printer: Printer
    private var mFormater: Flattener

    init {
        mFormater=logConfiguration.formater
        if (logConfiguration.printers.size>0){
            printer=PrinterSet(logConfiguration.printers.toTypedArray())
        }else{
            printer=PrinterSet(arrayOf(ConsolePrinter(mFormater)))
        }
    }

    override fun println(logLevel: Int, tag: String, msg: String) {
        printlnInternal(logLevel,tag,msg)
    }

    fun printlnInternal(logLevel: Int, tag: String, msg: String) {
        var thread = Thread.currentThread().id.toString()
        var stackTrace = ""
        val log = LogItem(logLevel, tag, thread, stackTrace, msg)
        if (logConfiguration.interceptors != null) {
            var log = LogItem(log)
            for (interceptor in logConfiguration.interceptors!!) {
                log = interceptor.intercept(log)
                if (log == null) {
                    return
                }
                check(!(log.tag == null || log.msg == null)) {
                    ("Interceptor " + interceptor
                            + " should not remove the tag or message of a log,"
                            + " if you don't want to print this log,"
                            + " just return a null when intercept.")
                }
            }
        }
        val content = log.msg
        printer?.println(
            log.level,
            log.tag,
            content
        )
    }

}
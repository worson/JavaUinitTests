package com.langogo.transcribe.comm.log

import com.langogo.transcribe.comm.log.format.DefaultFlattener
import com.langogo.transcribe.comm.log.format.Flattener
import com.langogo.transcribe.comm.log.internal.StackTraceUtil
import com.langogo.transcribe.comm.log.internal.SystemCompat
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import com.langogo.transcribe.comm.log.printer.FilePrinter
import com.langogo.transcribe.comm.log.printer.Printer
import com.langogo.transcribe.comm.log.printer.PrinterSet
import java.lang.StringBuilder

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

    override fun flush() {
        printer?.flush()
    }

    fun printlnInternal(logLevel: Int, tag: String, msg: String) {
        val log = LogItem(logLevel, tag, msg)
        if (logConfiguration.interceptors != null) {
            var log = LogItem(log)
            for (interceptor in logConfiguration.interceptors!!) {
                log = interceptor.intercept(log)
                if (log == null) {
                    return
                }
                check(!(log.tag == null || log.msg == null)) {
                    ("Interceptor " + interceptor
                            + " should not remove the tag or message of a log, if you don't want to print this log,just return a null when intercept.")
                }
            }
        }
        val sb = StringBuilder(log.tag)
        if (logConfiguration.withThread) {
            var thread = Thread.currentThread().id.toString()
            sb.append(" ${thread} ")
        }
        if (logConfiguration.withStackTrace) {
            sb.append(StackTraceUtil.getRuntimeCaller(logConfiguration.stackTraceDepth))
        }
        val content = log.msg
        printer?.println(
            log.level,
            sb.toString(),
            content
        )
    }

}
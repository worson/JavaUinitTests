package com.langogo.lib.log

import com.langogo.lib.log.format.Flattener
import com.langogo.lib.log.internal.StackTraceUtil
import com.langogo.lib.log.printer.ConsolePrinter
import com.langogo.lib.log.printer.Printer
import com.langogo.lib.log.printer.PrinterSet

/**
 * 说明:日志输出总入口
 * @author wangshengxing  07.17 2020
 */
class Logger(val logConfiguration: LogConfiguration) :
    Printer() {

    private var  printer: Printer
    private var mFormater: Flattener

    init {
        mFormater=logConfiguration.formater
        if (logConfiguration.printers.size>0){
            printer=
                PrinterSet(logConfiguration.printers.toTypedArray())
        }else{
            printer= PrinterSet(
                arrayOf(
                    ConsolePrinter(mFormater)
                )
            )
        }
    }

    override fun println(item: LogItem) {
        printlnInternal(item)
    }

    override fun flush() {
        printer?.flush()
    }

    fun printlnInternal(log: LogItem) {
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

        if (logConfiguration.withThread) {
            log.threadInfo = Thread.currentThread().id.toString()
        }
        if (logConfiguration.withStackTrace) {
            log.stackTraceInfo= StackTraceUtil.getRuntimeCaller(logConfiguration.stackTraceDepth)
        }

        printer?.println(
            log
        )
    }

}
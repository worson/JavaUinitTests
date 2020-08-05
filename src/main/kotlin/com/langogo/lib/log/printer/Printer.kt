package com.langogo.lib.log.printer

import com.langogo.lib.log.LogItem
import com.langogo.lib.log.LogLevel

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */


abstract open class Printer() {

    var logLevel= LogLevel.ALL

    abstract open fun println(item: LogItem)

    open fun realPrintln(content: String) {

    }

    abstract open fun flush(type:Int)

    fun v(tag: String, msg: () -> String) {
        if (LogLevel.VERBOSE<logLevel){
            return
        }
        println(
            LogItem(
                LogLevel.VERBOSE,
                tag,
                msg()
            )
        )
    }

    fun d(tag: String, msg: () -> String) {
        if (LogLevel.DEBUG<logLevel){
            return
        }
        println(
            LogItem(
                LogLevel.DEBUG,
                tag,
                msg()
            )
        )
    }

    fun i(tag: String, msg: String) {
        if (LogLevel.INFO<logLevel){
            return
        }
        println(
            LogItem(
                LogLevel.INFO,
                tag,
                msg
            )
        )
    }

    fun w(tag: String, msg: String) {
        if (LogLevel.WARN<logLevel){
            return
        }
        println(
            LogItem(
                LogLevel.WARN,
                tag,
                msg
            )
        )
    }

    fun e(tag: String, msg: String) {
        if (LogLevel.ERROR<logLevel){
            return
        }
        println(
            LogItem(
                LogLevel.ERROR,
                tag,
                msg
            )
        )
    }
}
package com.langogo.transcribe.comm

import com.langogo.transcribe.comm.log.LogConfiguration
import com.langogo.transcribe.comm.log.LogLevel
import com.langogo.transcribe.comm.log.Logger
import com.langogo.transcribe.comm.log.format.DefaultFlattener
import com.langogo.transcribe.comm.log.format.Flattener
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import com.langogo.transcribe.comm.log.printer.Printer
import com.langogo.transcribe.comm.log.printer.PrinterSet
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
object L {

    private var logLevel=LogLevel.INFO
    private var TAG_PREFIX = ""

    private lateinit  var mLogger: Logger

    init {
        L.init(
            LogConfiguration.Builder()
                .addPrinter(ConsolePrinter())
                .build()
        )
    }

    @JvmStatic
    fun init( configuration: LogConfiguration) {
        mLogger= Logger(configuration)
        this.logLevel=configuration.logLevel
        TAG_PREFIX=configuration.tag
        L.i("init#logLevel=${logLevel}")
    }

    @JvmStatic
    fun flush() {
        mLogger?.flush()
    }


    @JvmStatic
    fun d(tag: String, msg: Any) {
        log(priority = LogLevel.DEBUG, tag = "${TAG_PREFIX}#$tag", msg = msg)
    }


    @JvmStatic
    fun d(tag: String, msg:() -> Any) {
        log(priority = LogLevel.DEBUG, tag = "${TAG_PREFIX}#$tag", holder = msg)
    }

    @JvmStatic
    fun i(msg: Any?) {
        log(priority = LogLevel.INFO, tag = TAG_PREFIX, holder = { msg })
    }

    @JvmStatic
    fun i(tag: String, msg: Any?) {
        log(priority = LogLevel.INFO, tag = "${TAG_PREFIX}#$tag", holder = { msg })
    }

    @JvmStatic
    fun i(msg:() -> Any?) {
        log(priority = LogLevel.INFO, tag = TAG_PREFIX, holder = msg)
    }

    @JvmStatic
    fun i(tag: String, msg:() -> Any?) {
        log(priority = LogLevel.INFO, tag = "${TAG_PREFIX}#$tag", holder = msg)
    }

    @JvmStatic
    fun w(msg: Any?) {
        log(priority = LogLevel.WARN, tag = TAG_PREFIX, holder = { msg })
    }

    @JvmStatic
    fun w(tag: String, msg: Any?) {
        log(priority = LogLevel.WARN, tag = "${TAG_PREFIX}#$tag", holder = { msg })
    }

    @JvmStatic
    fun w(msg:() -> Any?) {
        log(priority = LogLevel.WARN, tag = TAG_PREFIX, holder = msg)
    }

    @JvmStatic
    fun w(tag: String, msg:() -> Any?) {
        log(priority = LogLevel.WARN, tag = "${TAG_PREFIX}#$tag", holder = msg)
    }

    @JvmStatic
    fun e(msg: Any?) {
        log(priority = LogLevel.ERROR, tag = TAG_PREFIX, holder = { msg })
    }

    @JvmStatic
    fun e(tag: String, msg: Any?) {
        log(priority = LogLevel.ERROR, tag = "${TAG_PREFIX}#$tag", holder = { msg })
    }
    @JvmStatic
    fun e(tag: String, msg: Any?, throwable: Throwable?) {
        log(
            priority = LogLevel.ERROR,
            tag = "${TAG_PREFIX}#$tag",
            msg = throwable?.let {
                "$msg\n${throwableToString(throwable)}"
            } ?: "throwable is null"
        )
    }


    private fun throwableToString(throwable: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()

        return sw.toString()
    }

    @JvmStatic
    private fun log(priority: Int, tag: String, msg: Any) {
        if (logLevel<=priority){
            val message = if (msg is Throwable) {
                val sw = StringWriter(256)
                val pw = PrintWriter(sw, false)
                msg.printStackTrace(pw)
                pw.flush()
                sw.toString()
            } else {
                msg?.toString() ?: "null"
            }
            mLogger?.println(logLevel,tag,message)
        }
    }


    @JvmStatic
    private fun log(priority: Int, tag: String, holder:() -> Any?) {
        if (logLevel<=priority){
            val msg=holder()
            val message = if (msg is Throwable) {
                val sw = StringWriter(256)
                val pw = PrintWriter(sw, false)
                msg.printStackTrace(pw)
                pw.flush()
                sw.toString()
            } else {
                msg?.toString() ?: "null"
            }
            mLogger?.println(logLevel,tag,message)
        }
    }
}
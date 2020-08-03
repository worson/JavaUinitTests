package com.langogo.lib.log

import com.langogo.lib.log.internal.Platform
import com.langogo.lib.log.printer.FilePrinter
import com.langogo.lib.log.printer.Printer
import com.langogo.lib.log.printer.file.DateFileNameGenerator
import com.langogo.lib.log.printer.file.FileNameGenerator
import com.langogo.lib.log.printer.file.handler.ZipLogHandler
import com.langogo.lib.log.printer.file.reporter.LogFileReporter
import com.langogo.lib.log.rpc.SocketClientPrinter
import com.langogo.lib.log.rpc.SocketSeverPrinterProxy
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
object L {

    private var logLevel= LogLevel.INFO
    private var TAG_PREFIX = ""

    private lateinit  var mLogger: Logger

    init {
        init(
            LogConfiguration.Builder()
                .addPrinter(Platform.get().defaultPrinter())
                .build()
        )
    }

    @JvmStatic
    fun init(debug:Boolean,logPath:File?){
        if (logPath==null){
            init(
                LogConfiguration.Builder()
                    .logLevel(if (debug) LogLevel.ALL else LogLevel.DEBUG)
                    .threadInfo(debug)
                    .traceInfo(debug, 6)
                    .addPrinter(Platform.get().defaultPrinter())
                    .addPrinter(SocketClientPrinter())
                    .build()
            )
        }else{
            var filePrinter:Printer?=null
            init(
                LogConfiguration.Builder()
                    .logLevel(if (debug) LogLevel.ALL else LogLevel.DEBUG)
                    .threadInfo(true)
                    .traceInfo(debug, 6)
                    .addPrinter(Platform.get().defaultPrinter())
                    .addPrinter(
                        FilePrinter.Builder(
                            File(logPath.absolutePath, "logging").absolutePath)
                            .fileNameGenerator(DateFileNameGenerator())
                            .logHandler(
                                ZipLogHandler(
                                    File(logPath.absolutePath, "backup").absolutePath,
                                    limitSize = 100 * 1024 * 1024,
                                    reporter = LogFileReporter()
                                )
                            )
                            .build().apply {
                                filePrinter=this
                            }
                    )
                    .build()
            )
            filePrinter?.let {
                SocketSeverPrinterProxy(it).start()
            }
        }


    }


    @JvmStatic
    fun init( configuration: LogConfiguration) {
        mLogger = Logger(configuration)
        logLevel =configuration.logLevel
        TAG_PREFIX =configuration.tag
        println("init#logLevel=$logLevel")
    }

    @JvmStatic
    fun flush() {
        mLogger?.flush()
    }

    @JvmStatic
    fun v(tag: String, msg:() -> Any) {
        log(
            priority = LogLevel.VERBOSE,
            tag = "$TAG_PREFIX#$tag",
            holder = msg
        )
    }

    @JvmStatic
    fun d( msg: Any) {
        log(
            priority = LogLevel.DEBUG,
            tag = TAG_PREFIX,
            msg = msg
        )
    }

    @JvmStatic
    fun d(tag: String, msg: Any) {
        log(
            priority = LogLevel.DEBUG,
            tag = "$TAG_PREFIX#$tag",
            msg = msg
        )
    }


    @JvmStatic
    fun d(tag: String, msg:() -> Any) {
        log(
            priority = LogLevel.DEBUG,
            tag = "$TAG_PREFIX#$tag",
            holder = msg
        )
    }

    @JvmStatic
    fun i(msg: Any?) {
        log(
            priority = LogLevel.INFO,
            tag = TAG_PREFIX,
            holder = { msg })
    }

    @JvmStatic
    fun i(tag: String, msg: Any?) {
        log(
            priority = LogLevel.INFO,
            tag = "$TAG_PREFIX#$tag",
            holder = { msg })
    }

    @JvmStatic
    fun i(msg:() -> Any?) {
        log(
            priority = LogLevel.INFO,
            tag = TAG_PREFIX,
            holder = msg
        )
    }

    @JvmStatic
    fun i(tag: String, msg:() -> Any?) {
        log(
            priority = LogLevel.INFO,
            tag = "$TAG_PREFIX#$tag",
            holder = msg
        )
    }

    @JvmStatic
    fun w(msg: Any?) {
        log(
            priority = LogLevel.WARN,
            tag = TAG_PREFIX,
            holder = { msg })
    }

    @JvmStatic
    fun w(tag: String, msg: Any?) {
        log(
            priority = LogLevel.WARN,
            tag = "$TAG_PREFIX#$tag",
            holder = { msg })
    }

    @JvmStatic
    fun w(msg:() -> Any?) {
        log(
            priority = LogLevel.WARN,
            tag = TAG_PREFIX,
            holder = msg
        )
    }

    @JvmStatic
    fun w(tag: String, msg:() -> Any?) {
        log(
            priority = LogLevel.WARN,
            tag = "$TAG_PREFIX#$tag",
            holder = msg
        )
    }

    @JvmStatic
    fun e(msg: Any?) {
        log(
            priority = LogLevel.ERROR,
            tag = TAG_PREFIX,
            holder = { msg })
    }

    @JvmStatic
    fun e(tag: String, msg: Any?) {
        log(
            priority = LogLevel.ERROR,
            tag = "$TAG_PREFIX#$tag",
            holder = { msg })
    }
    @JvmStatic
    fun e(tag: String, msg: Any?, throwable: Throwable?) {
        log(
            priority = LogLevel.ERROR,
            tag = "$TAG_PREFIX#$tag",
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
        if (logLevel <=priority){
            val message = if (msg is Throwable) {
                val sw = StringWriter(256)
                val pw = PrintWriter(sw, false)
                msg.printStackTrace(pw)
                pw.flush()
                sw.toString()
            } else {
                msg?.toString() ?: "null"
            }
            mLogger?.println(LogItem(priority, tag, message))
        }
    }


    @JvmStatic
    private fun log(priority: Int, tag: String, holder:() -> Any?) {
        if (logLevel <=priority){
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
            mLogger?.println(LogItem(priority, tag, message))
        }
    }
}
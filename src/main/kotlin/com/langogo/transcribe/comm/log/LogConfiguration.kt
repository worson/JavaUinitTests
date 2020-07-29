package com.langogo.transcribe.comm.log

import com.langogo.transcribe.comm.log.format.DefaultFlattener
import com.langogo.transcribe.comm.log.format.Flattener
import com.langogo.transcribe.comm.log.printer.Printer
import java.util.*


/**
 * 说明:初始化类
 * @author wangshengxing  07.17 2020
 */
class LogConfiguration(val logLevel:Int = 0,val tag: String ,
                       val withThread:Boolean = false,
                       val withStackTrace:Boolean = false,val stackTraceDepth:Int = 0,
                       val interceptors: List<Interceptor>? = null,
                       val printers: List<Printer>,
                       val formater: Flattener
                       ) {


    class Builder {

        private var logLevel = DEFAULT_LOG_LEVEL

        private var tag = DEFAULT_TAG

        private var withThread = false

        private var withStackTrace = false

        private var stackTraceDepth = 6

        private var formater: Flattener = DefaultFlattener()

        private var printers:MutableList<Printer> = mutableListOf()

        private var interceptors: MutableList<Interceptor> = mutableListOf()



        fun logLevel(logLevel: Int): Builder {
            this.logLevel = logLevel
            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }
        fun threadInfo(isOpen:Boolean): Builder {
            withThread = isOpen
            return this
        }
        fun traceInfo(isOpen:Boolean, depth: Int=6): Builder {
            withStackTrace=isOpen
            stackTraceDepth=depth
            return this
        }

        fun formater(formater: Flattener): Builder {
            this.formater=formater
            return this
        }

        fun addPrinter(printer: Printer): Builder {
            printers.add(printer)
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            if (interceptors == null) {
                interceptors = ArrayList()
            }
            interceptors?.add(interceptor)
            return this
        }
        fun interceptors(interceptors: MutableList<Interceptor>): Builder {
            this.interceptors = interceptors
            return this
        }

        fun build(): LogConfiguration {
            return LogConfiguration(logLevel,tag,withThread,withStackTrace,stackTraceDepth,interceptors,printers,formater)
        }

        companion object {
            private const val DEFAULT_LOG_LEVEL = LogLevel.ALL
            private val DEFAULT_TAG: String = "LLOG"
        }
    }
}
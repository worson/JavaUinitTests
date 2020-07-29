package com.langogo.transcribe.comm.log.printer

import com.langogo.transcribe.comm.log.format.DefaultFlattener
import com.langogo.transcribe.comm.log.format.Flattener

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class ConsolePrinter(val formater: Flattener= DefaultFlattener()) :Printer {

    override fun println(logLevel: Int, tag: String, msg: String) {
        println(formater.flatten(logLevel,tag,msg))
    }

    override fun flush() {
    }
}
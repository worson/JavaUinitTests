package com.langogo.transcribe.comm.log.printer

import com.langogo.transcribe.comm.log.LogItem
import com.langogo.transcribe.comm.log.format.BasicFlattener
import com.langogo.transcribe.comm.log.format.RawFlattener
import com.langogo.transcribe.comm.log.format.Flattener

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class ConsolePrinter(val formater: Flattener= BasicFlattener()) :Printer {

    override fun println(item: LogItem) {
        println(formater.flatten(item))
    }

    override fun flush() {
    }
}
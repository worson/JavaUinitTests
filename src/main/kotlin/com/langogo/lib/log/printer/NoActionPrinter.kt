package com.langogo.lib.log.printer

import com.langogo.lib.log.LogItem
import com.langogo.lib.log.format.BasicFlattener
import com.langogo.lib.log.format.RawFlattener
import com.langogo.lib.log.format.Flattener

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class ConsolePrinter(val formater: Flattener = BasicFlattener()) :
    Printer() {

    override fun println(item: LogItem) {
        println(formater.flatten(item))
    }

    override fun flush(type:Int) {
    }
}
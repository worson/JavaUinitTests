package com.langogo.lib.log.printer

import com.langogo.lib.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
abstract open class Printer() {

    abstract open fun println(item: LogItem)

    open fun realPrintln(content:String){

    }

    abstract open fun flush()
}
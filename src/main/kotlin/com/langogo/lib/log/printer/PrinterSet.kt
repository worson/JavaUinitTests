package com.langogo.lib.log.printer

import com.langogo.lib.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class PrinterSet(val printers: Array<Printer>):
    Printer {

    override fun println(item: LogItem) {
        for (printer in printers) {
            printer.println(item)
        }
    }

    override fun flush() {
        for (printer in printers) {
            printer?.flush()
        }
    }
}
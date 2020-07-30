package com.langogo.transcribe.comm.log.printer

import com.langogo.transcribe.comm.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class PrinterSet(val printers: Array<Printer>):Printer {

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
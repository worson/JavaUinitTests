package com.langogo.transcribe.comm.log.printer

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class PrinterSet(val printers: Array<Printer>):Printer {

    override fun println(logLevel: Int, tag: String, msg: String) {
        for (printer in printers) {
            printer.println(logLevel, tag, msg)
        }
    }

    override fun flush() {
        for (printer in printers) {
            printer?.flush()
        }
    }
}
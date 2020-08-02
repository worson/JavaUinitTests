package com.langogo.lib.log.rpc

import com.langogo.lib.log.LogItem
import com.langogo.lib.log.format.BasicFlattener
import com.langogo.lib.log.format.Flattener
import com.langogo.lib.log.printer.Printer

/**
 * 说明:
 * @author wangshengxing  07.31 2020
 */
class SocketClientPrinter(var flattener: Flattener = BasicFlattener() ): Printer() {

    override fun println(item: LogItem) {

    }

    override fun flush() {

    }
}
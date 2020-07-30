package com.langogo.lib.log.printer

import com.langogo.lib.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface Printer {

    fun println(item: LogItem)

    fun flush()
}
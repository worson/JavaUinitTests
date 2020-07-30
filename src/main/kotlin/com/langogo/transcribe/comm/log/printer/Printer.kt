package com.langogo.transcribe.comm.log.printer

import com.langogo.transcribe.comm.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface Printer {

    fun println(item: LogItem)

    fun flush()
}
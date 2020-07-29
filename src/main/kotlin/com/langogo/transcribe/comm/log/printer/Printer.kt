package com.langogo.transcribe.comm.log.printer

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface Printer {

    fun println(logLevel: Int, tag: String, msg: String)

    fun flush()
}
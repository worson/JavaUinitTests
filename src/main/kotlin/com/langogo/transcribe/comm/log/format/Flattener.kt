package com.langogo.transcribe.comm.log.format

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface Flattener {

    fun flatten(logLevel: Int, tag: String?, message: String?): String
}
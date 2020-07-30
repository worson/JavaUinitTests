package com.langogo.transcribe.comm.log.format

import com.langogo.transcribe.comm.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface Flattener {

    fun flatten(item: LogItem): String
}
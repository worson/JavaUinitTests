package com.langogo.transcribe.comm.log.format

import com.langogo.transcribe.comm.log.LogItem
import com.langogo.transcribe.comm.log.LogLevel

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class RawFlattener:Flattener {

    override fun flatten(item: LogItem): String {
        return "${item.tag}:${item.msg}"
    }

}
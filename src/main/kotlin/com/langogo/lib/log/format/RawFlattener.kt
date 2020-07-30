package com.langogo.lib.log.format

import com.langogo.lib.log.LogItem
import com.langogo.lib.log.LogLevel

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class RawFlattener: Flattener {

    override fun flatten(item: LogItem): String {
        return "${item.tag}:${item.msg}"
    }

}
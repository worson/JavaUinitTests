package com.langogo.lib.log.format

import com.langogo.lib.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface Flattener {

    fun flatten(item: LogItem): String
}
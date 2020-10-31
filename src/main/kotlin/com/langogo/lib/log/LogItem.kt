package com.langogo.lib.log

/**
 * 说明:日志结点
 * @author wangshengxing  07.17 2020
 */
class LogItem {

    var level: Int

    var tag: String

    var msg: String

    var time: Long=System.currentTimeMillis()

    fun basicLength():Int{
        return tag.length+msg.length+20
    }

    var threadInfo: String? = null

    var stackTraceInfo: String? = null

    constructor(level: Int, tag: String, msg: String) {
        this.level = level
        this.tag = tag
        this.msg = msg
    }

    constructor(
        item: LogItem
    ) {
        this.level = item.level
        this.tag = item.tag
        this.threadInfo = item.threadInfo
        this.stackTraceInfo = item.stackTraceInfo
        this.msg = item.msg
    }

    constructor(
        level: Int,
        tag: String,
        threadInfo: String?,
        stackTraceInfo: String?,
        msg: String
    ) {
        this.level = level
        this.tag = tag
        this.threadInfo = threadInfo
        this.stackTraceInfo = stackTraceInfo
        this.msg = msg
    }
}
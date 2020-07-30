package com.langogo.transcribe.comm.log.format

import com.langogo.transcribe.comm.log.LogItem
import com.langogo.transcribe.comm.log.LogLevel
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class BasicFlattener:Flattener {

    val dataFormat= SimpleDateFormat("yyyy:MM:dd HH:mm:ss.SSS", Locale.US)

    override fun flatten(item: LogItem): String {
        val sb = StringBuilder(item.basicLength())
        sb.append(" ")
        sb.append(LogLevel.getShortLevelName(item.level))
        if (item.threadInfo!=null) {
            sb.append(" ")
            sb.append(item.threadInfo)
            sb.append(" ")
        }

        sb.append(dataFormat.format(Date(item.time)))
        sb.append(" ")

        sb.append(item.tag)
        if (item.stackTraceInfo!=null) {
            sb.append(item.stackTraceInfo)
        }
        sb.append(": ")

        sb.append(item.msg)
        return sb.toString()
    }

}
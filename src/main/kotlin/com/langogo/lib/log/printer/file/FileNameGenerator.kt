package com.langogo.lib.log.printer.file

import java.text.SimpleDateFormat
import java.util.*


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
interface FileNameGenerator {

    fun generateFileName(logLevel: Int, timestamp: Long): String
}


class DateFileNameGenerator :
    FileNameGenerator {
    var mLocalDateFormat: ThreadLocal<SimpleDateFormat> =
        object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                return SimpleDateFormat("yyyy_MM_dd___HH_mm_ss", Locale.US)
            }
        }

    override fun generateFileName(logLevel: Int, timestamp: Long): String {
        val sdf = mLocalDateFormat.get()
        sdf.timeZone = TimeZone.getDefault()
        return "log__${sdf.format(Date(timestamp))}.log"
    }
}
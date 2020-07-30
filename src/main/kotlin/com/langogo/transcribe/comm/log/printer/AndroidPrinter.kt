package com.langogo.transcribe.comm.log.printer

import com.langogo.transcribe.comm.log.LogItem
import com.langogo.transcribe.comm.log.format.BasicFlattener
import com.langogo.transcribe.comm.log.format.Flattener
import java.lang.reflect.Method

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class AndroidPrinter(private val maxChunkSize: Int = DEFAULT_MAX_CHUNK_SIZE, val formater: Flattener = BasicFlattener()) : Printer {
    var printer: Method?=null

    init {
        val cls=Class.forName("android.util.Log")
        val print=cls?.getMethod("println", Int::class.javaPrimitiveType,String::class.java,String::class.java)
        printer=print
    }

    override fun flush() {

    }

    override fun println(
        item: LogItem
    ) {
        val msg=item.msg
        val tag=if (item.stackTraceInfo==null){
            item.tag
        }else{
            item.tag+item.stackTraceInfo
        }
        if (item.msg.length <= maxChunkSize) {
            printChunk(item.level,tag , item.msg)
            return
        }
        val msgLength = msg.length
        var start = 0
        var end: Int
        while (start < msgLength) {
            end = adjustEnd(
                msg,
                start,
                Math.min(start + maxChunkSize, msgLength)
            )
            printChunk(item.level, tag, msg.substring(start, end))
            start = end
        }
    }

    /**
     * Print single chunk of log in new line.
     *
     * @param logLevel the level of log
     * @param tag      the tag of log
     * @param msg      the msg of log
     */
    fun printChunk(logLevel: Int, tag: String, msg: String) {
        printer?.invoke(null,logLevel,tag,msg)
    }

    companion object {
        /**
         * Generally, android has a default length limit of 4096 for single log, but
         * some device(like HUAWEI) has its own shorter limit, so we just use 2048
         * and wish it could run well in all devices.
         */
        const val DEFAULT_MAX_CHUNK_SIZE = 2048

        /**
         * Move the end to the nearest line separator('\n') (if exist).
         */
        fun adjustEnd(msg: String, start: Int, originEnd: Int): Int {
            if (originEnd == msg.length) {
                // Already end of message.
                return originEnd
            }
            if (msg[originEnd] == '\n') {
                // Already prior to '\n'.
                return originEnd
            }
            // Search back for '\n'.
            var last = originEnd - 1
            while (start < last) {
                if (msg[last] == '\n') {
                    return last + 1
                }
                last--
            }
            return originEnd
        }
    }

}
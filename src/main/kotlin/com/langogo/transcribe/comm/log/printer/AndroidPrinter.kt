package com.langogo.transcribe.comm.log.printer

import com.langogo.transcribe.comm.log.LogItem

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class AndroidPrinter
/**
 * Constructor.
 *
 *
 * If single message is too long, it will be separated to several chunks automatically, the max
 * size of each chunk default to be {@value #DEFAULT_MAX_CHUNK_SIZE}, you can specify the
 * maxChunkSize using [.AndroidPrinter].
 */ @JvmOverloads constructor(private val maxChunkSize: Int = DEFAULT_MAX_CHUNK_SIZE) :
    Printer {

    override fun flush() {
    }

    override fun println(
        item: LogItem
    ) {
        val msg=item.msg
        if (item.msg.length <= maxChunkSize) {
            printChunk(item.level, item.tag, item.msg)
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
            printChunk(item.level, item.tag, msg.substring(start, end))
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
    fun printChunk(logLevel: Int, tag: String?, msg: String?) {
        // TODO: 2020/7/15
//        android.util.Log.println(logLevel, tag, msg)
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
    /**
     * Constructor.
     *
     * @param maxChunkSize the max size of each chunk. If the message is too long, it will be
     * separated to several chunks automatically
     * @since 1.4.1
     */
}
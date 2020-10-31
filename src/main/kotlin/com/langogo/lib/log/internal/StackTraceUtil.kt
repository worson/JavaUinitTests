package com.langogo.lib.log.internal

import java.io.PrintWriter

import java.io.StringWriter
import java.net.UnknownHostException


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
object StackTraceUtil {

    val XLOG_STACK_TRACE_ORIGIN = "333"

    /**
     * Get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * Get the real stack trace and then crop it with a max depth.
     *
     * @param stackTrace the full stack trace
     * @param maxDepth   the max depth of real stack trace that will be cropped, 0 means no limitation
     * @return the cropped real stack trace
     */
    fun getCroppedRealStackTrack(
        stackTrace: Array<StackTraceElement>,
        stackTraceOrigin: String?,
        maxDepth: Int
    ): Array<StackTraceElement?>? {
        return cropStackTrace(
            getRealStackTrack(
                stackTrace,
                stackTraceOrigin
            ),
            maxDepth
        )
    }

    /**
     * Get the real stack trace, all elements that come from XLog library would be dropped.
     *
     * @param stackTrace the full stack trace
     * @return the real stack trace, all elements come from system and library user
     */
    private fun getRealStackTrack(
        stackTrace: Array<StackTraceElement>,
        stackTraceOrigin: String?
    ): Array<StackTraceElement?> {
        var ignoreDepth = 0
        val allDepth = stackTrace.size
        var className: String
        for (i in allDepth - 1 downTo 0) {
            className = stackTrace[i].className
            if (className.startsWith(XLOG_STACK_TRACE_ORIGIN!!)
                || stackTraceOrigin != null && className.startsWith(stackTraceOrigin)
            ) {
                ignoreDepth = i + 1
                break
            }
        }
        val realDepth = allDepth - ignoreDepth
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth)
        return realStack
    }

    /**
     * Crop the stack trace with a max depth.
     *
     * @param callStack the original stack trace
     * @param maxDepth  the max depth of real stack trace that will be cropped,
     * 0 means no limitation
     * @return the cropped stack trace
     */
    private fun cropStackTrace(
        callStack: Array<StackTraceElement?>,
        maxDepth: Int
    ): Array<StackTraceElement?>? {
        var realDepth = callStack.size
        if (maxDepth > 0) {
            realDepth = Math.min(maxDepth, realDepth)
        }
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(callStack, 0, realStack, 0, realDepth)
        return realStack
    }

    /**
     * 指印当前堆栈信息
     */
    fun getCurrentStackInfo(): String {
        val stackTrace =
            Thread.currentThread().stackTrace
        val sb = StringBuilder()
        stackTrace.forEach {
            element ->
            val methodName = element.methodName
            val lineNumber = element.lineNumber
            sb.append("(${element.fileName}:${lineNumber})\n")
        }
        return sb.toString()
    }

    /**
     * 获取打日志位置所在的类，行号，方法名，只在debug的时候输出
     */
    fun getRuntimeCaller(maxDepth: Int): String {
        val stackTrace =
            Thread.currentThread().stackTrace
        val index: Int =
            getStackOffset(
                stackTrace,
                maxDepth
            )
        if (index == -1) {
            return "[]"
        }
        val element = stackTrace[index]
        val methodName = element.methodName
        val lineNumber = element.lineNumber
        return "(${element.fileName}:${lineNumber})"
//        return "[(${element.fileName}:${lineNumber})#${methodName}]"
    }




    private fun getStackOffset(stackTrace: Array<StackTraceElement>,maxDepth: Int): Int {
        if (null != stackTrace) {
            if (stackTrace.size > maxDepth) {
                return maxDepth
            }
            return stackTrace.size-1
        }
        return -1
    }
}
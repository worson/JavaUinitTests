package com.langogo.lib.log.internal

import com.langogo.lib.log.LogConfiguration
import com.langogo.lib.log.LogLevel
import com.langogo.lib.log.Logger

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */
 object  LogDebug {

    var IS_DEBUG = false
    var TRACE_DEPTH = 5

    val debugLogger:Logger by lazy {
        val debug=IS_DEBUG
        Logger(
            LogConfiguration.Builder()
                .logLevel(if (debug) LogLevel.ALL else LogLevel.WARN)
                .threadInfo(debug)
                .traceInfo(debug, TRACE_DEPTH)
                .addPrinter(Platform.get().defaultPrinter())
                .build())
    }
}
package com.langogo.lib.log.internal

import com.langogo.lib.log.LogConfiguration
import com.langogo.lib.log.LogLevel
import com.langogo.lib.log.Logger

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */

internal object  LogDebug {

    internal var IS_DEBUG = false

    val debugLogger:Logger by lazy {
        val debug=IS_DEBUG
        Logger(
            LogConfiguration.Builder()
                .logLevel(if (debug) LogLevel.ALL else LogLevel.WARN)
                .threadInfo(debug)
                .traceInfo(debug, 5)
                .addPrinter(Platform.get().defaultPrinter())
                .build())
    }
}
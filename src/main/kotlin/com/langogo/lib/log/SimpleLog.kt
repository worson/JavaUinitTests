package com.langogo.lib.log

import com.langogo.lib.log.internal.Platform
import com.langogo.lib.log.rpc.SocketClientPrinter

/**
 * 说明:
 * @author wangshengxing  08.03 2020
 */
object SimpleLog {

    fun initConsolePrint(debug:Boolean){
        L.init(
            LogConfiguration.Builder()
                .logLevel(if (debug) LogLevel.ALL else LogLevel.DEBUG)
                .threadInfo(debug)
                .traceInfo(debug, 6)
                .addPrinter(Platform.get().defaultPrinter())
                .build()
        )
    }
}
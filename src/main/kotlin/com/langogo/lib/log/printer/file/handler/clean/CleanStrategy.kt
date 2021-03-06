package com.langogo.lib.log.printer.file.handler.clean

import java.io.File




/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
interface CleanStrategy {

    /**
     * Whether we should clean a specified log file.
     *
     * @param file the log file
     * @return true is we should clean the log file
     */
    fun shouldClean(file: File): Boolean
}
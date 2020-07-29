package com.langogo.transcribe.comm.log.printer.file.handler

import java.io.File

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
abstract open class LogHandler(private val fileDir:String) {

    init {
        checkFolder()
    }

    private fun checkFolder() {
        val folder = File(fileDir)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    abstract open fun onLogHandle( logfile:File, isFlush:Boolean)



    fun clearCaches(){

    }
}
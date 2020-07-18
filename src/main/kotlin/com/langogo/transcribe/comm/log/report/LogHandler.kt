package com.langogo.transcribe.comm.log.report

import java.io.File

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
open class LogHandler(private val fileDir:String) {

    init {
        checkFolder()
    }

    private fun checkFolder() {
        val folder = File(fileDir)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    /**
     * 处理日志数据
     */
    open fun onLogHandle( logfile:File, isFlush:Boolean){
        if (!logfile.exists()){
            return
        }
        println("onLogHandle#${logfile.absoluteFile}")
        val backFile = File(fileDir,logfile.name)
        if (backFile.exists()) {
            backFile.delete()
        }
        logfile.renameTo(backFile)
    }


    fun clearCaches(){

    }
}
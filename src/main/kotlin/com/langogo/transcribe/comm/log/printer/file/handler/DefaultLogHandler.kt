package com.langogo.transcribe.comm.log.printer.file.handler

import java.io.File


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class DefaultLogHandler(val fileDir:String):LogHandler(fileDir) {

    /**
     * 处理日志数据
     */
    override fun onLogHandle( logfile:File, isFlush:Boolean){
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
}
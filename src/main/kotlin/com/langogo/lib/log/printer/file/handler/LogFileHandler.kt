package com.langogo.lib.log.printer.file.handler

import com.langogo.lib.log.internal.LogDebug
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributeView

/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
abstract open class LogFileHandler(private val fileDir:String) {
    private val  TAG = "LogFileHandler"
    private val L = LogDebug.debugLogger

    init {
        checkFolder()
    }

    private fun checkFolder() {
        val folder = File(fileDir)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    abstract open fun onLogHandle( logfile:File, isFlush:Boolean,flushType:Int)

    /**
     * 返回log文件列表
     */
    protected fun logFiles():List<File>{
        val dirFile = File(fileDir)
        val files=dirFile.listFiles().apply {
            sortBy {
                it.lastModified()
            }
        }.toList().filter { it.isFile && (!it.extension.endsWith("zip")) }
        return files
    }


    /**
     * 删除所有日志文件
     */
    fun deleteLogFiles(){
        logFiles()?.forEach{
            if (it.exists()) {
                it.delete()
            }
        }
    }

    /**
     * 根据大小限定日志文件
     */
    protected fun filesLimitCut(limitSize:Long=20*1024*1024){
//        println("filesLimitCut：limitSize=$limitSize")
        var files=logFiles()
        var totalSize=files.fold(0L){
            a,f ->
            a+f.length()
        }
        if (totalSize>limitSize){
            val targetSize=limitSize/2
            L.i(TAG,"filesLimitCut：totalSize=$totalSize,targetSize=${targetSize}")
            while (totalSize>targetSize){
                files?.first()?.delete()
                files=logFiles()
                totalSize=files?.fold(0L){
                        a,f ->
                    a+f.length()
                }
            }
        }

    }
}
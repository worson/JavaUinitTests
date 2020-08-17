package com.langogo.lib.log.printer.file.reporter

import com.langogo.lib.log.printer.file.handler.LogFileHandler
import com.langogo.lib.log.printer.file.handler.ZipLogHandler
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributeView

/**
 * 说明:文件上报
 * @author wangshengxing  07.29 2020
 */
open class LogFileReporter(val uploadDir:File) {

    data class ReportItem(val file: File,val flushType:Int){

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
        while (totalSize>limitSize){
//            println("filesLimitCut：totalSize=$totalSize")
            files?.first()?.delete()
            files=logFiles()
            totalSize=files?.fold(0L){
                    a,f ->
                a+f.length()
            }
        }
    }

    /**
     * 返回log文件列表
     */
    protected fun logFiles():List<File>{
        val dirFile = uploadDir
        val files=dirFile.listFiles().apply {
            sortBy {
                it.lastModified()
            }
        }.toList()
        return files
    }


    open fun onReport(handler: LogFileHandler, item: ReportItem) {
        println("onReport: ${item} ")
    }
}
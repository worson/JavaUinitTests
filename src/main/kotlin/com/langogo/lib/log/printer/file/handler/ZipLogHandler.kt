package com.langogo.lib.log.printer.file.handler

import com.langogo.lib.log.internal.LogDebug
import com.langogo.lib.log.printer.file.reporter.LogFileReporter
import com.langogo.lib.log.printer.file.zipper.FileZipper
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class ZipLogHandler(val fileDir:String,val limitSize:Long=100*1024*1024,val password:String="heyan1234",val reporter: LogFileReporter):
    LogFileHandler(fileDir) {
    private val  TAG = "ZipLogHandler"
    private val L = LogDebug.debugLogger

    private val tmpDir:File=File(fileDir,"tmp")
    private val compressFile:File=File(tmpDir,"log.zip")
    private val lestCacheSize= min(limitSize/2,30*1024*1024)

    init {
        checkTmpDir()
    }

    private fun checkTmpDir(){
        if (!tmpDir.exists()) {
            tmpDir.mkdirs()
        }

    }

    override fun onLogHandle(logfile: File, isFlush: Boolean,flushType:Int) {
        L.i(TAG,"onLogHandle#isFlush=$isFlush , filePath=${logfile.absolutePath}, lastModify=${SimpleDateFormat("yyyy:MM:dd HH:mm:ss.SSS", Locale.US).format(
            Date(logfile.lastModified())
        )}")
        //备份日志
        if (logfile.exists()){
            filesLimitCut(limitSize)
            val backFile = File(fileDir,logfile.name)
            if (backFile.exists()) {
                backFile.delete()
            }
            logfile.renameTo(backFile)
        }
        //触发日志上报
        if (isFlush){
            val files=logFiles()
            L.i(TAG,"all file ${files}")
            if (files.size>0){
                checkTmpDir()
                if (tmpDir.exists()) {
                    FileZipper.compress(files,compressFile,password)
                    reporter?.onReport(
                        this,
                        LogFileReporter.ReportItem(compressFile,flushType))
                }
                deleteLogFiles()
//                filesLimitCut(lestCacheSize)
            }
        }


    }
}
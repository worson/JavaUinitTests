package com.langogo.transcribe.comm.log.printer.file.handler

import com.langogo.transcribe.comm.log.printer.file.reporter.LogFileReporter
import com.langogo.transcribe.comm.log.printer.file.zipper.FileZipper
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributeView
import java.util.*


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class ZipLogHandler(val fileDir:String,val reporter: LogFileReporter):LogHandler(fileDir) {

    private val mergeFile:File=File(fileDir,"merge.txt")
    private val compressFile:File=File(fileDir,"log.zip")

    override fun onLogHandle(logfile: File, isFlush: Boolean) {
        println("onLogHandle#isFlush=$isFlush , filePath=${logfile.absolutePath}")
        //备份日志
        if (logfile.exists()){
            val backFile = File(fileDir,logfile.name)
            if (backFile.exists()) {
                backFile.delete()
            }
            logfile.renameTo(backFile)
        }
        //触发日志上报
        if (isFlush){
            val dirFile = File(fileDir)
            val files=dirFile.listFiles().apply {
                sortBy {
                    val p: Path = Paths.get(it.absolutePath)
                    val ab=Files.getFileAttributeView(p, BasicFileAttributeView::class.java).readAttributes()
                    ab.creationTime().toMillis()
                }

            }
            if (files.size>0){
                FileZipper.compress(files.toList(),compressFile,"heyan1234")
            }

            println("all file ${Arrays.toString(files)}")
        }


    }
}
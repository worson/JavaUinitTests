package com.langogo.transcribe.comm.log.report

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
class ZipLogHandler(val fileDir:String):LogHandler(fileDir) {

    private val mergeFile:File=File(fileDir,"merge.txt")

    override fun onLogHandle(logfile: File, isFlush: Boolean) {
        super.onLogHandle(logfile, isFlush)
        println("onLogHandle#isFlush$isFlush")
        val dirFile = File(fileDir)
        val files=dirFile.listFiles().apply {
            sortBy {
                val p: Path = Paths.get(it.absolutePath)
                val ab=Files.getFileAttributeView(p, BasicFileAttributeView::class.java).readAttributes()
                ab.creationTime().toMillis()
            }
        }
        println("all file ${Arrays.toString(files)}")
    }
}
package com.langogo.transcribe.comm.log.printer.file.handler

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

    /**
     * 返回log文件列表
     */
    protected fun logFiles():List<File>{
        val dirFile = File(fileDir)
        val files=dirFile.listFiles().apply {
            sortBy {
                val p: Path = Paths.get(it.absolutePath)
                val ab= Files.getFileAttributeView(p, BasicFileAttributeView::class.java).readAttributes()
                ab.creationTime().toMillis()
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
}
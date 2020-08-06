package com.test.libtest.log
import com.langogo.lib.log.L
import com.langogo.lib.log.printer.file.handler.LogFileHandler
import com.langogo.lib.log.printer.file.reporter.LogFileReporter
import java.io.File

/**
 * 说明:
 * @author wangshengxing  08.05 2020
 */
class S3LogUploader(dirFile:File) : LogFileReporter(dirFile) {
    val  TAG = "S3LogUploader"
    interface LogFileInterceptorListener{

        fun interceptFile(item: ReportItem):Boolean=false
    }

    private val interceptListener: MutableMap<Int,LogFileInterceptorListener> = HashMap()

    fun putInterceptorListener(flushType:Int,listener:LogFileInterceptorListener){
        interceptListener.put(flushType,listener)
    }

    override fun onReport(handler: LogFileHandler, item: ReportItem) {
        super.onReport(handler, item)
        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }
        val newFile = File(uploadDir,"${item.file.nameWithoutExtension}_${logFiles().size}.${item.file.extension}")
        L.i(TAG, "onReport: newFile=${newFile.absolutePath}")
        if (item.file.exists()) {
            item.file.renameTo(newFile)
        }
        val newItem=ReportItem(newFile,item.flushType)
        var intercept=false
        interceptListener.get(item.flushType)?.interceptFile(newItem)?.let {
            intercept=it
        }
        if (!intercept){
            L.i(TAG, "onReport: not intercept, delete ${newItem}")
        }
    }





    companion object {
        const val FLUSH_TYPE_NOMAL=0
        const val FLUSH_TYPE_SHARE=1
        const val FLUSH_TYPE_UPLOAD=2


    }
}
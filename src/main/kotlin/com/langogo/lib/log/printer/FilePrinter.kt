package com.langogo.lib.log.printer

import com.langogo.lib.log.LogItem
import com.langogo.lib.log.LogLevel
import com.langogo.lib.log.format.BasicFlattener
import com.langogo.lib.log.format.Flattener
import com.langogo.lib.log.printer.file.DateFileNameGenerator
import com.langogo.lib.log.printer.file.FileNameGenerator
import com.langogo.lib.log.printer.file.backup.BackupStrategy
import com.langogo.lib.log.printer.file.backup.FileSizeBackupStrategy
import com.langogo.lib.log.printer.file.handler.DefaultLogHandler
import com.langogo.lib.log.printer.file.handler.LogFileHandler
import java.io.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
class FilePrinter internal constructor(builder: Builder) :
    Printer() {

    class Builder( var folderPath: String ) {
        var fileNameGenerator: FileNameGenerator =
            DateFileNameGenerator()
        var backupStrategy: BackupStrategy =
            FileSizeBackupStrategy(10 * 1024 * 1024)
        var flattener: Flattener =
            BasicFlattener()
        var logHandler: LogFileHandler? = null

        fun fileNameGenerator(fileNameGenerator: FileNameGenerator): Builder {
            this.fileNameGenerator = fileNameGenerator
            return this
        }

        fun backupStrategy(backupStrategy: BackupStrategy): Builder {
            this.backupStrategy = backupStrategy
            return this
        }

        fun flattener(flattener: Flattener): Builder {
            this.flattener = flattener
            return this
        }

        fun logHandler(logHandler: LogFileHandler): Builder {
            this.logHandler = logHandler
            return this
        }

        fun build(): FilePrinter {
            return FilePrinter(this)
        }
    }

    private val folderPath: String

    private val fileNameGenerator: FileNameGenerator

    private val backupStrategy: BackupStrategy

    private val flattener: Flattener

    private val writer: Writer

    private val logHandler: LogFileHandler?

    @Volatile
    private var worker: Worker? = null

    private var needFlush = false
    private var flushType = -1

    companion object {

        private const val USE_WORKER = true
    }

    init {
        logHandler = builder.logHandler
        folderPath = builder.folderPath //+File.separator+"logging"
        fileNameGenerator = builder.fileNameGenerator
        backupStrategy = builder.backupStrategy
        flattener = builder.flattener
        writer = Writer()
        if (USE_WORKER) {
            worker = Worker()
        }
        checkLogFolder()
    }



    private fun checkLogFolder() {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    override fun println(item: LogItem) {
        val timeMillis = System.currentTimeMillis()
        if (USE_WORKER) {
            if (!worker!!.isStarted()) {
                worker?.start()
            }
            worker?.enqueue(item)
        } else {
            doPrintln(item)
        }
    }

    override fun flush(type:Int) {
        needFlush=true
        flushType=type
        println(
            LogItem(
                LogLevel.INFO,
                "FilePrinter",
                "flush"
            )
        )
    }

    /**
     * Do the real job of writing log to file.
     */
    private fun doPrintln(
        item: LogItem
    ) {
        var lastFileName = writer.lastFileName
        val flush=needFlush
        if (lastFileName == null || needFlush) {
            val folder = File(folderPath)
            folder.list()?.let {
                list ->
                val len =list.size
                list.forEachIndexed { index, s ->
                    if (index < len-1){
                        logHandler?.onLogHandle(File(folderPath,s),false,flushType)
                    }else{
                        logHandler?.onLogHandle(File(folderPath,s),flush,flushType)
                    }
                }
            }
            //还没有文件
            lastFileName= openNewLog(item.level)
        }
        val lastFile = writer.file!!
        if (backupStrategy.shouldBackup(lastFile)) {
            if (writer.isOpened) {
                writer.close()
            }
            logHandler?.onLogHandle(File(lastFileName),flush,flushType)
            lastFileName= openNewLog(item.level)
        }
        val flattenedLog = flattener.flatten(item)
        writer.appendLog(flattenedLog)
    }

    override fun realPrintln(content: String) {
        writer?.appendLog(content)
    }

    fun openNewLog(logLevel: Int):String?{
        var lastFileName = writer.lastFileName
        val newFileName =
            fileNameGenerator.generateFileName(logLevel, System.currentTimeMillis())
        if (newFileName != lastFileName) {
            if (writer.isOpened) {
                writer.close()
            }
            if (!writer.open(newFileName)) {
                return null
            }
            return newFileName
        }
        return newFileName
    }








    /**
     * Work in background, we can enqueue the logs, and the worker will dispatch them.
     */
    private inner class Worker : Runnable {
        private val logs: BlockingQueue<LogItem> = LinkedBlockingQueue()

        @Volatile
        private var started = false

        /**
         * Enqueue the log.
         *
         * @param log the log to be written to file
         */
        fun enqueue(log: LogItem) {
            try {
                logs.put(log)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        /**
         * Whether the worker is started.
         *
         * @return true if started, false otherwise
         */
        fun isStarted(): Boolean {
            synchronized(this) { return started }
        }

        /**
         * Start the worker.
         */
        fun start() {
            synchronized(this) {
                Thread(this).start()
                started = true
            }
        }

        override fun run() {
            var log: LogItem
            try {
                while (logs.take().also { log = it } != null) {
                    doPrintln(log)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                synchronized(this) { started = false }
            }
        }
    }

    /**
     * Used to write the flattened logs to the log file.
     */
    private inner class Writer {
        /**
         * Get the name of last used log file.
         * @return the name of last used log file, maybe null
         */
        /**
         * The file name of last used log file.
         */
        var lastFileName: String? = null
            private set

        /**
         * Get the current log file.
         *
         * @return the current log file, maybe null
         */
        /**
         * The current log file.
         */
        var file: File? = null
            private set
        private var bufferedWriter: BufferedWriter? = null

        /**
         * Whether the log file is opened.
         *
         * @return true if opened, false otherwise
         */
        val isOpened: Boolean
            get() = bufferedWriter != null

        /**
         * Open the file of specific name to be written into.
         *
         * @param newFileName the specific file name
         * @return true if opened successfully, false otherwise
         */
        fun open(newFileName: String?): Boolean {
            lastFileName = newFileName
            file = File(folderPath, newFileName)

            // Create log file if not exists.
            if (!file!!.exists()) {
                try {
                    val parent = file!!.parentFile
                    if (!parent.exists()) {
                        parent.mkdirs()
                    }
                    file!!.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    lastFileName = null
                    file = null
                    return false
                }
            }
            // Create buffered writer.
            try {
                bufferedWriter = BufferedWriter(FileWriter(file, true))
            } catch (e: Exception) {
                e.printStackTrace()
                lastFileName = null
                file = null
                return false
            }
            return true
        }

        /**
         * Close the current log file if it is opened.
         *
         * @return true if closed successfully, false otherwise
         */
        fun close(): Boolean {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                } finally {
                    bufferedWriter = null
                    lastFileName = null
                    file = null
                }
            }
            return true
        }

        /**
         * Append the flattened log to the end of current opened log file.
         *
         * @param flattenedLog the flattened log
         */
        fun appendLog(flattenedLog: String?) {
            try {
                bufferedWriter?.write(flattenedLog)
                bufferedWriter?.newLine()
                bufferedWriter?.flush()
            } catch (e: IOException) {
            }
        }
    }


}
package com.langogo.transcribe.comm.log.printer.file.backup

import java.io.File




/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
interface BackupStrategy {

    fun shouldBackup(file: File): Boolean
}
package com.langogo.transcribe.comm.log.printer.file.zipper

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File

/**
 * 说明:
 * @author wangshengxing  07.29 2020
 */
object FileZipper{

    /*
        压缩文件
     */
    fun compress(inputFiles:List<File>,outfile:File,passwd:String){
        if (outfile.exists()) {
            outfile.delete()
        }
        val parameters = ZipParameters()
        parameters.compressionMethod = CompressionMethod.DEFLATE // 压缩方式
        parameters.compressionLevel = CompressionLevel.NORMAL // 压缩级别
        parameters.isEncryptFiles = true
        parameters.encryptionMethod = EncryptionMethod.ZIP_STANDARD // 加密方式
        ZipFile(outfile,passwd.toCharArray()).apply {
            isRunInThread=true
            addFiles(inputFiles,parameters)
        }
    }
}
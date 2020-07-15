package com.test.file.zip

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import net.lingala.zip4j.model.enums.EncryptionMethod
import org.junit.Test
import java.io.File
import kotlin.system.measureTimeMillis


/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class TestZip4j {
    @Test
    fun testCompressZip() {
        ZipFile("src/main/res/test/file/zip/hello.zip").addFile("src/main/res/test/file/zip/hello.txt")
//        Assert.assertEquals(11, channel.size())

    }

    @Test
    fun testCompressZipWithPassword() {
        val targetFile = File("src/main/res/test/file/zip/hello_password.zip")
        if (targetFile.exists()) {
            targetFile.delete()
        }
        val cost = measureTimeMillis {
            val passwd="password"
            val parameters = ZipParameters()
            parameters.compressionMethod = CompressionMethod.DEFLATE // 压缩方式
            parameters.compressionLevel = CompressionLevel.NORMAL // 压缩级别
            parameters.isEncryptFiles = true
            parameters.encryptionMethod = EncryptionMethod.ZIP_STANDARD // 加密方式
            ZipFile(targetFile,passwd.toCharArray()).addFile("src/main/res/test/file/zip/hello.txt",parameters)
        }
        println("testCompressZipWithPassword cost : $cost")

    }
}
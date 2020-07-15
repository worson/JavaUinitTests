package com.test.nio.mmap

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.junit.Test
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import kotlin.system.measureTimeMillis

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class TestMmapWrite {

    /*
     result:stdCost=4011,stdBufferedCost=77,mmapCost=103
     */
    @Test
    fun compareDirectAndMmapWrite() {
        val times=1_000_000
        val msg="compareDirectAndMmapWrite\n"
        val stdCost= measureTimeMillis {
            stdWrite("src/main/res/test/nio/mmap/stdWrite.txt",times,msg)
        }
        val stdBufferedCost= measureTimeMillis {
            stdBufferedWrite("src/main/res/test/nio/mmap/stdBufferedWrite.txt",times,msg)
        }

        val mmapCost= measureTimeMillis {
            stdMmapWrite("src/main/res/test/nio/mmap/mmapWrite.txt",times,msg)
        }

        println("stdCost=$stdCost,stdBufferedCost=$stdBufferedCost,mmapCost=$mmapCost")
    }

    fun stdWrite(name:String,times:Int,msg:String){
        val file = File(name)
        if (file.exists()) {
            file.delete()
        }
        FileOutputStream(file).use {
            os ->
            val data=msg.toByteArray()
            for (i in 0 .. times){
               os.write(data)
            }
        }
    }

    fun stdBufferedWrite(name:String,times:Int,msg:String){
        val file = File(name)
        if (file.exists()) {
            file.delete()
        }
        FileOutputStream(file).use {
            BufferedOutputStream(it,8192*10).use {
                    os ->
                val data=msg.toByteArray()
                for (i in 0 .. times){
                    os.write(data)
                }
                os.flush()
            }
        }
    }

    fun stdMmapWrite(name:String,times:Int,msg:String){
        val file = File(name)
        if (file.exists()) {
            file.delete()
        }
        val totalSize=msg.toByteArray().size*times
        RandomAccessFile(file, "rw").use {
            val map=it.channel.map(FileChannel.MapMode.READ_WRITE,0,totalSize.toLong())
            val data=msg.toByteArray()
            for (i in 0 .. times-1){
                map.put(data)
            }
            map.force()
            it.channel.close()
        }

    }


}
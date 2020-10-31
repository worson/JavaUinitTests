package com.test.nio.filechannel

import junit.framework.Assert
import org.junit.Test
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

/**
 * 说明:
 * @author wangshengxing  07.31 2020
 */
class FileChannelLockUnitTest {

    @Test
    fun writeFileWithLock(){
        val file = "src/main/res/test/nio/test_write_with_lock.txt"
        RandomAccessFile(file, "rw").use { writer ->
            writer.channel.use { channel ->
                val buff =
                    ByteBuffer.wrap("Hello world".toByteArray(StandardCharsets.UTF_8))
                channel.write(buff)
                buff.flip()
                channel.write(buff)
                // verify
                val reader = RandomAccessFile(file, "r")
                reader.close()
            }
        }
    }

}
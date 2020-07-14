package com.test.nio.filechannel

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets


/**
 * 说明:
 * @author wangshengxing  07.14 2020
 */
class FileChannelUinitTest {

    @Test
    fun test(){
        println("test")
    }

    @Test
    @Throws(IOException::class)
    fun givenFile_whenReadWithFileChannelUsingRandomAccessFile_thenCorrect() {
        RandomAccessFile("src/main/res/test/nio/test_read.in", "r").use { reader ->
            reader.channel.use { channel ->
                ByteArrayOutputStream().use { out ->
                    var bufferSize = 1024
                    if (bufferSize > channel.size()) {
                        bufferSize = channel.size().toInt()
                    }
                    val buff: ByteBuffer = ByteBuffer.allocate(bufferSize)
                    while (channel.read(buff) > 0) {
                        out.write(buff.array(), 0, buff.position())
                        buff.clear()
                    }
                    val fileContent =
                        String(out.toByteArray(), StandardCharsets.UTF_8)
                    assertEquals("Hello world", fileContent)
                }
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun whenWriteWithFileChannelUsingRandomAccessFile_thenCorrect() {
        val file = "src/main/res/test/nio/test_write_using_filechannel.txt"
        RandomAccessFile(file, "rw").use { writer ->
            writer.channel.use { channel ->
                val buff =
                    ByteBuffer.wrap("Hello world".toByteArray(StandardCharsets.UTF_8))
                channel.write(buff)
                // verify
                val reader = RandomAccessFile(file, "r")
                assertEquals("Hello world", reader.readLine())
                reader.close()
            }
        }
    }

    @Test
    @Throws(IOException::class)
    fun whenGetFileSize_thenCorrect() {
        val reader = RandomAccessFile("src/main/res/test/nio/test_read.in", "r")
        val channel = reader.channel

        // the original file size is 11 bytes.
        assertEquals(11, channel.size())
        channel.close()
        reader.close()
    }

    @Test
    @Throws(IOException::class)
    fun whenTruncateFile_thenCorrect() {
        val input = "this is a test input"
        val fout = FileOutputStream("src/main/res/test/nio/test_truncate.txt")
        var channel = fout.channel
        val buff = ByteBuffer.wrap(input.toByteArray())
        channel.write(buff)
        buff.flip()
        channel = channel.truncate(5)
        assertEquals(5, channel.size())
        fout.close()
        channel.close()
    }

    @Test
    @Throws(IOException::class)
    fun givenFile_whenReadAFileSectionIntoMemoryWithFileChannel_thenCorrect() {
        RandomAccessFile("src/main/res/test/nio/test_read.in", "r").use { reader ->
            reader.channel.use { channel ->
                ByteArrayOutputStream().use { out ->
                    val buff = channel.map(FileChannel.MapMode.READ_ONLY, 6, 5)
                    if (buff.hasRemaining()) {
                        val data = ByteArray(buff.remaining())
                        buff[data]
                        assertEquals("world", String(data, StandardCharsets.UTF_8))
                    }
                }
            }
        }
    }


}
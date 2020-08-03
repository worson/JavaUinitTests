package com.test.libtest.log

import com.langogo.lib.log.L
import com.langogo.lib.log.SimpleLog
import com.langogo.lib.log.rpc.SocketPacketUtil
import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * 说明:
 * @author wangshengxing  08.02 2020
 */
class LogPacketUtilTest {

    val TAG = "LogPacketUitilTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    @Test
    fun showAsciiStrByteSize() {
        val str = "hello"
        L.i(TAG, "showAsciiStrByteSize: ${str.toByteArray(Charsets.UTF_8).size}")
    }

    @Test
    fun showChineseStrByteSize() {
        val str = "中国"
        L.i(TAG, "showAsciiStrByteSize: ${str.toByteArray(Charsets.UTF_8).size}")
    }

    @Test
    //单个包的生成与解析
    fun testPackAndUnpack() {
        val expect = "hello"
        val strBytes = SocketPacketUtil.pack(expect)
        L.i(TAG, "testPackAndUnpack: bytes size ${strBytes.size}")
        val bf = ByteBuffer.allocate(strBytes.size)
        bf.put(strBytes)
        L.i(TAG, "testPackAndUnpack: write bytebuffer size ${bf.position()}")
        bf.flip()
        L.i(TAG, "testPackAndUnpack: for read bytebuffer size ${bf.limit()}")
        val result = SocketPacketUtil.unpack(bf)
        L.i(TAG, "testPackAndUnpack: for read bytebuffer size ${bf.position()}")
        Assert.assertEquals(expect, result)
    }

    @Test
    //同一个ByteBuffer反复载入与解包
    fun testRepeatPackAndUnpack() {
        val times = 5
        val bf = ByteBuffer.allocate(100)
        for (i in 1..times) {
            val expect = "${i}:hello"
            val strBytes = SocketPacketUtil.pack(expect)
            bf.put(strBytes)
            bf.flip()
            val result = SocketPacketUtil.unpack(bf)
            Assert.assertEquals(expect, result)
            L.i(TAG, "testRepeatPackAndUnpack: ${bf}")
            L.i(TAG, "testRepeatPackAndUnpack: remaining=${bf.remaining()}")
            //相当于恢复成创建的时候的
            bf.clear()
        }

    }

    @Test
    //同一个ByteBuffer可能存在多个包的结构，对应粘包的现象
    fun testMulitiPackAndUnpack() {
        var expect = mutableListOf<String>()
        val totalArray = ByteArrayOutputStream()
        val strTimes = 5
        for (i in 1..strTimes) {
            val str = "hello:${i}\n"
            expect.add(str)
            val t = SocketPacketUtil.pack(str)
            totalArray.write(t)
        }
        val strBytes = totalArray.toByteArray()

        var result = mutableListOf<String>()
        val bf = ByteBuffer.allocate(strBytes.size)
        bf.put(strBytes)
        bf.flip()
        var tStr = SocketPacketUtil.unpack(bf)
        while (tStr != null) {
            result.add(tStr)
            tStr = SocketPacketUtil.unpack(bf)
        }
        L.i(TAG, "testMulitiPackAndUnpack: result ${result}")
        Assert.assertEquals(expect, result)
    }


    @Test
    //测试非完整包的生成与解析，主要用来模拟分包的现象
    fun testHalfPackAndUnpack() {
        val bf = ByteBuffer.allocate(1000)
        var expect = mutableListOf<String>()

        var str = ""

        str = "hello:${1}\n"
        expect.add(str)
        val t1 = SocketPacketUtil.pack(str)
        bf.put(t1)

        str = "hello:${1}\n"
        expect.add(str)

        val t2 = SocketPacketUtil.pack(str)
        val t2_1 = t2.copyOfRange(0, 10)
        val t2_2 = t2.copyOfRange(10, t2.size)
        bf.put(t2_1)


        var result = mutableListOf<String>()
        var tStr:String? = ""

        bf.flip()
        tStr = SocketPacketUtil.unpack(bf)
        while (tStr != null) {
            result.add(tStr)
            tStr = SocketPacketUtil.unpack(bf)
        }

        bf.compact()
        bf.put(t2_2)

        bf.flip()
        tStr = SocketPacketUtil.unpack(bf)
        while (tStr != null) {
            result.add(tStr)
            tStr = SocketPacketUtil.unpack(bf)
        }


        L.i(TAG, "testMulitiPackAndUnpack: result ${result}")
        Assert.assertEquals(expect, result)
    }
}
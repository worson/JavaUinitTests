package com.test.nio.bytebuffer

import com.langogo.lib.log.L
import com.langogo.lib.log.SimpleLog
import org.junit.Assert
import org.junit.Test
import java.nio.ByteBuffer

/**
 * 说明:
 * @author wangshengxing  08.02 2020
 */
class ByteBufferUnitTest {

    val  TAG = "ByteBufferUnitTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    @Test
    //确认写数据时的position
    fun testShowPosition(){
        val bf = ByteBuffer.allocate(20)
        L.i(TAG, "testShowPosition: init position ${bf.position()}")
        bf.putInt(100)
        L.i(TAG, "testShowPosition: init position ${bf.position()}")
    }

    @Test
    //确认limit功能的作用
    fun testShowLimit(){
        val bf = ByteBuffer.allocate(20)
        L.i(TAG, "testShowLimit: oringin limit ${bf.limit()}")
        bf.limit(10)
        L.i(TAG, "testShowLimit: new  limit ${bf.limit()}")
    }

    @Test
    fun testRewind(){
        val bf = ByteBuffer.allocate(20)
        val expect=100
        L.i(TAG, "testRewind: origin  ${bf.remaining()}")
        bf.putInt(expect)
        L.i(TAG, "testRewind: ${bf.remaining()}")
        bf.rewind()
        val result = bf.getInt()
        Assert.assertEquals(expect,result)
    }

    @Test
    //标准的数据写和读切换
    fun testWriteAndGet(){
        val bf = ByteBuffer.allocate(20)
        val expect=100
        bf.putInt(expect)
        bf.flip()
        val result = bf.getInt()
        Assert.assertEquals(expect,result)
    }

    @Test
    fun testArrayGet(){
        val bf = ByteBuffer.allocate(20)
        val expect=100
        bf.putInt(expect)
        var result = bf.getInt(0)
        result = bf.getInt(0)
        Assert.assertEquals(expect,result)
    }

    @Test
    //数据的向头部的平移
    fun testCompact(){
        val bf = ByteBuffer.allocate(20)
        val expect=100
        bf.putInt(expect)
        bf.putInt(expect)
        L.i(TAG, "testContra: origin position ${bf.position()}")
        bf.flip()
        val result = bf.getInt()
        bf.compact()
        L.i(TAG, "testContra: new  position ${bf.position()}")
        Assert.assertEquals(expect,result)
    }

    @Test
    //测试：写-读(未读完)-写-读的场景
    fun testBufferUnread(){
        val bf = ByteBuffer.allocate(20)
        var result = 0
        val expect1=100
        val expect2=111
        //写入两个数据
        bf.putInt(expect1)
        bf.putInt(expect2)
        L.i(TAG, "testBufferUnread: after write read ${bf.position()}")

        //第一次读数据，但是只读一个
        bf.flip()
        result = bf.getInt()
        Assert.assertEquals(expect1,result)

        L.i(TAG, "testBufferUnread: after first read ${bf}")
        bf.compact()
        //此时可以直接写数据了
        L.i(TAG, "testBufferUnread: after compact : ${bf}")

        //开始第二次写数据
        val expect3=222
        bf.putInt(expect3)
        L.i(TAG, "testBufferUnread: after rewrite : ${bf}")

        //准备重新开始读
        //第一次读数据，但是只读一个
        bf.flip()
        L.i(TAG, "testBufferUnread: before  reread : ${bf}")

        result = bf.getInt()
        Assert.assertEquals(expect2,result)

        result = bf.getInt()
        Assert.assertEquals(expect3,result)

    }
}
package com.test.socket.localsocket

import com.langogo.lib.log.L
import com.langogo.lib.log.SimpleLog
import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  08.01 2020
 */
class SocektSingleReceiveUnitTest {

    val  TAG = "SocektSingleReceiveUnitTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    @Test
    fun testBlockSendHello(){
        val server = BlockingServerSocketService()
        server.start()
        Thread.sleep(500)
        val clent = ClientSocket("127.0.0.1")
        clent.start()
        Thread.sleep(500)
        clent.send("hello,server!".toByteArray())
        clent.send("hello,server!".toByteArray())
        clent.send("hello,server!".toByteArray())
        Thread.sleep(500)
    }

    @Test
    fun testNioReceiveHello(){
        val server = ServerSocketService()
        server.start()
        Thread.sleep(500)
        val clent = ClientSocket("127.0.0.1")
        clent.start()
        Thread.sleep(500)

        clent.send("hello,server!".toByteArray())
        Thread.sleep(50)
        clent.send("hello,server!".toByteArray())
        Thread.sleep(50)
        clent.send("hello,server!".toByteArray())
        Thread.sleep(50
        )
        Thread.sleep(1500)
    }
}
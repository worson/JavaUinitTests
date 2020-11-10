package com.test.libtest.log

import com.langogo.lib.log.L
import com.langogo.lib.log.LogItem
import com.langogo.lib.log.SimpleLog
import com.langogo.lib.log.printer.ConsolePrinter
import com.langogo.lib.log.printer.Printer
import com.langogo.lib.log.rpc.SocketClientPrinter
import com.langogo.lib.log.rpc.SocketSeverPrinterProxy
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

/**
 * 说明:测试C/S日志发送接收服务
 * @author wangshengxing  08.02 2020
 */
class SocketLogUnitTest {
    val TAG = "LogPacketUitilTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    @Test
    //服务端未启动时企图打印日志，只要没有异常就说明成功
    fun testPrintWhenServerNotStart(){
        var expect=StringBuilder()
        val client= SocketClientPrinter()
        val times=1000
        for (i in 1 .. times){
            val msg="testSendNommalLog: cnt=${i}\n"
            expect.append(msg)
            client.realPrintln(msg)
        }
        client.flush(0)
    }

    @Test
    //验证小log的大小是否正常
    fun testSendNommalLog(){
        var receiveCnt=AtomicInteger(0)
        var expect=StringBuilder()
        var result=StringBuilder()
        val server= SocketSeverPrinterProxy(object : Printer(){
            override fun println(item: LogItem) {
            }

            override fun flush(type:Int) {
            }

            override fun realPrintln(content: String) {
                receiveCnt.incrementAndGet()
                result.append(content)
            }
        })
        server.start()
        Thread.sleep(50)
        val client= SocketClientPrinter()
        val times=1000
        for (i in 1 .. times){
            val msg="testSendNommalLog: cnt=${i}\n"
            expect.append(msg)
            client.realPrintln(msg)
        }
        Thread.sleep(500)

        Assert.assertEquals(times,receiveCnt.get())
        Assert.assertEquals(expect.toString(),result.toString())
        server.stop()
        while (server.connected()) {
            Thread.sleep(1000)
        }
    }

    @Test
    fun testSendNommalLogReat(){
        testSendNommalLog()
    }


    @Test
    //验证超大log的大小是否正常
    fun testSendHugeLog(){
        Thread.sleep(1000)
        var receiveCnt=AtomicInteger(0)
        var expect=StringBuilder()
        var result=StringBuilder()
        val server= SocketSeverPrinterProxy(object : Printer(){
            override fun println(item: LogItem) {
            }

            override fun flush(type:Int) {
            }

            override fun realPrintln(content: String) {
                receiveCnt.incrementAndGet()
                result.append(content)
            }
        })
        server.start()
        Thread.sleep(50)
        val client= SocketClientPrinter()
        val times=10
        for (i in 1 .. times){
            val sb = StringBuilder()
            val msg="testSendNommalLog: cnt=${i}"
            for (j in 1 .. (1024*100/msg.length)){
                sb.append(msg)
                sb.append("\n\n\n")
            }
            val rStr=sb.toString()
            expect.append(rStr)
            client.realPrintln(rStr)
        }
        Thread.sleep(1500)

        //次数肯定是不等的
//        Assert.assertEquals(times,receiveCnt.get())
        Assert.assertEquals(expect.toString(),result.toString())
        server.stop()
        while (server.connected()) {
            Thread.sleep(1000)
        }
    }

}
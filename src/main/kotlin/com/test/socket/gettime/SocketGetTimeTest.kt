package com.test.socket.gettime

import com.langogo.lib.log.L
import com.langogo.lib.log.SimpleLog
import org.junit.Test
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * 说明:
 * @author wangshengxing  08.02 2020
 */
class SocketGetTimeTest {
    val  TAG = "SocketGetTimeTest"

    init {
        SimpleLog.initConsolePrint(true)
    }

    class TestServerSocket(val bindPort:Int=22612){

        val  TAG = "TestServerSocket"
        private val serverSocket = ServerSocket()
        private val started= AtomicBoolean(false)

        fun start(){

            if (started.get()){
                L.w(TAG, "start: server is running ")
                return
            }
            L.i(TAG, "start: ")
            started.set(true)
            thread {
                serverSocket.bind(InetSocketAddress(bindPort))
                L.i(TAG, "start: bind server port ${bindPort} ")
                serverSocket.use {
                    while (started.get()){
                        val socket=serverSocket.accept()
                        L.i(TAG, " accept socket")
                        socket.getOutputStream().use {
                            it.write("hello,client!".toByteArray())
                            it.flush()
                        }
                    }
                    L.i(TAG, "start: sever is shutdown")
                }

            }



        }

        fun stop(){
            if (started.get()) {
                L.i(TAG, "stop: ")
                started.set(true)
            }
        }
    }

    class TestClientSocket(val ip:String, val bindPort:Int=22612) {

        val  TAG = "TestClientSocket"

        private val started= AtomicBoolean(false)
        private val socket = Socket()
        private var inputStream: InputStream? = null
        private var outputBlockQueue= LinkedBlockingDeque<ByteArray>()

        fun start(){
            if (started.get()){
                L.w(TAG, "start: client is running ")
                return
            }
            L.i(TAG, "start: ")
            started.set(true)
            thread {
                socket.connect(InetSocketAddress(ip,bindPort))
                socket.use {
                    inputStream= socket.getInputStream()
                    inputStream.use {
                        inputStream?.readBytes()?.let{
                                result ->
                            L.i(TAG, "start: get input ${String(result)} ")

                        }
                    }

                }
                L.i(TAG, "start: client socket closed ")

            }
        }

        fun stop(){
            if (started.get()) {
                L.i(TAG, "stop: ")
                started.set(true)
                inputStream=null
            }
        }
    }


    @Test
    fun testGetCurrentTime(){
        val server = TestServerSocket()
        server.start()
        Thread.sleep(500)
        val clent = TestClientSocket("127.0.0.1")
        clent.start()
        Thread.sleep(500)
    }
}
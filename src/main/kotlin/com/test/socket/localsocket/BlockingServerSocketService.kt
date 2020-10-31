package com.test.socket.localsocket

import com.langogo.lib.log.L
import java.io.BufferedInputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * 说明:
 * @author wangshengxing  08.01 2020
 */
class BlockingServerSocketService(val port:Int=22612):BaseServerSocketService(port) {
    override val  TAG = "ServerSocketService"

    private val serverSocket = ServerSocket()


    override fun start(){

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
                    socket.getInputStream().use {
                        BufferedInputStream(it).use {
                                ins ->
                            val result=ins.readBytes()
                            L.i(TAG, "start: get input ${String(result)} ")
                        }
                    }
                }
                L.i(TAG, "start: sever is shutdown")
            }

        }



    }


}
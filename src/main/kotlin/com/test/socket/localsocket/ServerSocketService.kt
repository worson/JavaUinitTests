package com.test.socket.localsocket

import com.langogo.lib.log.L
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import kotlin.concurrent.thread

/**
 * 说明:
 * @author wangshengxing  08.01 2020
 */
class ServerSocketService(val port: Int = 22612) : BaseServerSocketService(port) {
    override val TAG = "ServerSocketService"

    private var serverSocket:ServerSocket?=null


    override fun start() {

        if (started.get()) {
            L.w(TAG, "start: server is running ")
            return
        }
        L.i(TAG, "start: server")
        started.set(true)
        thread {

            val serverChannel = ServerSocketChannel.open()
            serverSocket = serverChannel.socket()
            serverSocket?.bind(InetSocketAddress(bindPort))
            L.i(TAG, "bind: port ${bindPort}")
            serverChannel.configureBlocking(false)
            val selector = Selector.open()
            serverChannel.register(selector, SelectionKey.OP_ACCEPT)
            L.i(TAG, "selector: regested ")
            while (started.get()) {
                selector.select()
                val readyKeys = selector.selectedKeys()
                val iterator = readyKeys.iterator()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    iterator.remove()
                    try {
                        if (key.isAcceptable) {
                            val server = key.channel() as ServerSocketChannel
                            val client = server.accept()
                            L.i(TAG, "start: accept from client ")
                            client.configureBlocking(false)

                            //SelectionKey.OP_WRITE
                            val clientKey = client.register(selector, SelectionKey.OP_READ)
                            val buffer = ByteBuffer.allocate(1024)
                            clientKey.attach(buffer)
                        }

                        if (key.isReadable) {
                            val client = key.channel() as SocketChannel
                            val output = key.attachment() as ByteBuffer
                            output.clear()
                            client.read(output)
                            L.i(TAG, "start: output info ${output}")
                            ByteArray(output.position()).let {
                                output.flip()
                                output.get(it)
                                L.i(TAG, "start: received data ${String(it)}")
                            }

                        }

                    } catch (e: Exception) {
                        L.i(TAG, "channel oparate exception : ${e}")
                        key.cancel()
                        try {
                            key.channel().close()
                        } catch (e: Exception) {
                        }
                    }
                }
//                L.i(TAG, "start: select query end")
            }
            L.i(TAG, "sever is shutdown")
        }

    }


}
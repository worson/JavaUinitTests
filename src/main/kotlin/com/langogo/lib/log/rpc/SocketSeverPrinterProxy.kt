package com.langogo.lib.log.rpc

import com.langogo.lib.log.internal.LogDebug
import com.langogo.lib.log.printer.Printer
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


/**
 * 说明:
 * @author wangshengxing  07.31 2020
 */
class SocketSeverPrinterProxy(
    val delegate: Printer,
    val bindPort: Int = PacketProtocol.DEFAULT_SERVER_PORT
) {
    val TAG = "SocketSeverPrinterProxy"

    private val L = LogDebug.debugLogger

    protected val started = AtomicBoolean(false)
    protected val connected = AtomicBoolean(false)

    private var serverSocket: ServerSocket? = null

    fun start() {

        if (started.get()) {
            L.w(TAG, "start: server is running ")
            return
        }
        L.i(TAG, "start: server")
        started.set(true)
        thread {
            handleStartLoop()
        }

    }

    fun stop() {
        if (started.get()) {
            L.i(TAG, "stop: server")
            started.set(false)
        }
    }

    fun connected():Boolean{
        return connected.get()
    }

    private fun handleStartLoop() {
        val serverChannel = ServerSocketChannel.open()
        serverSocket = serverChannel.socket()
        serverSocket?.bind(InetSocketAddress(bindPort))
        connected.set(true)
        L.i(TAG, "handleStart#bind: port ${bindPort}")
        serverChannel.configureBlocking(false)
        val selector = Selector.open()
        serverChannel.register(selector, SelectionKey.OP_ACCEPT)
        L.i(TAG, "handleStart#selector: registed ")
        while (started.get()) {
            val selected = selector.select(2000)
//            L.i(TAG, "start: selected number=${selected}")
            if (selected>0){
                handleEvent(selector)
            }
        }
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            L.e(TAG, "handleStartLoop: server close error ${e.localizedMessage}")
        }
        started.set(false)
        connected.set(false)
        L.i(TAG, "handleStart#sever shutdown ... , binded=${serverSocket?.isBound}, closed = ${serverSocket?.isClosed}")
    }

    private fun handleEvent(selector: Selector) {
        val readyKeys = selector.selectedKeys()
        val iterator = readyKeys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            iterator.remove()
            try {
                if (key.isAcceptable) {
                    handleAccept(key)
                }
                if (key.isConnectable) {
                    handleConnect(key)
                }

                if (key.isReadable) {
                    handleRead(key)
                }

            } catch (e: Exception) {
                L.e(TAG, "handleEvent#channel oparate exception : ${e.localizedMessage}")
                key.cancel()
                try {
                    key.channel().close()
                } catch (e: Exception) {
                    L.e(TAG, "handleEvent:close exception ${e.localizedMessage} ")
                }
            }
        }
    }

    private fun handleRead(key: SelectionKey) {
        val client = key.channel() as SocketChannel
        val buffer = key.attachment() as ByteBuffer
        if (!client.isConnected) {
            L.i(TAG, "handleRead: socket is disconnected")
            try {
                client.close()
            } catch (e: Exception) {
            }
            return
        }
        L.v(TAG, { "handleRead: isReadable ,before read ${buffer}" })
        //优先读取数据
        val readSize = client.read(buffer)
        L.v(TAG, { "handleRead: isReadable ,after read ,readSize=${readSize}, buffer=${buffer}" })
        buffer.flip()
        if (readSize < 0) {
            L.i(TAG, "handleRead: read buffer no data ")
            try {
                client.close()
            } catch (e: Exception) {
                L.e(TAG, "handleRead: close error ${e.localizedMessage}")
            }
            return
        }

        //如果是粘包问题，有两种情况，未截断时：一个包需要解析多析多次
        //出现截断时：尾端未能解析的数据要留到下一次才能解析
        var msg: String? = if (buffer.remaining() > 0) SocketPacketUtil.unpack(buffer) else null
        while (msg != null) {
            L.i(TAG, "handleRead:handle log :${msg} ")
            delegate?.realPrintln(msg)
            msg = if (buffer.remaining() > 0) SocketPacketUtil.unpack(buffer) else null
        }
        L.v(TAG, { "handleRead: isReadable ,before compact ${buffer}" })
        buffer.compact()
        L.v(TAG, { "handleRead: isReadable ,after compact ${buffer}" })
    }

    private fun handleConnect(key: SelectionKey) {
        val ch = key.channel() as SocketChannel
        if (ch.finishConnect()) {
            L.i(TAG, "handleConnect: finishConnect")
        } else {
            L.i(TAG, "handleConnect: Connect failed")
        }
    }

    private fun handleAccept(key: SelectionKey) {
        val selector = key.selector()
        val server = key.channel() as ServerSocketChannel
        val client = server.accept()
        L.i(TAG, "handleAccept: accept from client ")
        client.configureBlocking(false)

        L.i(TAG, "handleAccept: isConnected ${client.isConnected}")
        //SelectionKey.OP_WRITE
        val clientKey = client.register(selector, SelectionKey.OP_READ)

        val buffer = ByteBuffer.allocate(PacketProtocol.MAX_PACKET_SIZE * 10)
        L.i(TAG, "handleAccept from client: set buffer ${buffer}")
        clientKey.attach(buffer)

//        client.register(selector, SelectionKey.OP_ACCEPT)
    }




}
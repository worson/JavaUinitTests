package com.langogo.lib.log.rpc

import com.langogo.lib.log.LogItem
import com.langogo.lib.log.format.BasicFlattener
import com.langogo.lib.log.format.Flattener
import com.langogo.lib.log.internal.LogDebug
import com.langogo.lib.log.printer.Printer
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread
import kotlin.math.min

/**
 * 说明:
 * @author wangshengxing  07.31 2020
 */
class SocketClientPrinter(var flattener: Flattener = BasicFlattener() ): Printer() {
    private val L = LogDebug.debugLogger

    private val maxChunkSize: Int = PacketProtocol.MAX_PACKET_DATA_SIZE

    class LogClientSocket(val ip:String=PacketProtocol.DEFAULT_SERVER_IP, val bindPort:Int=PacketProtocol.DEFAULT_SERVER_PORT) {
        private val L = LogDebug.debugLogger
        val  TAG = "ClientSocekt"
        private val MAX_LOG_CACHE_SIZE=5000
        private val started= AtomicBoolean(false)
        private lateinit var socket :Socket
        private var outputStream: OutputStream? = null
        private var outputBlockQueue= LinkedBlockingDeque<ByteArray>(MAX_LOG_CACHE_SIZE)

        fun start(){
            if (started.get()){
                L.w(TAG, "start: client is running ")
                return
            }
            L.i(TAG, "start: ")
            started.set(true)
            thread {
                if (handleSocketConnect()) {
                    handleLogSendLoop()
                    started.set(false)
                    L.i(TAG, "start: client socket closed ")
                }else{
                    started.set(false)
                }
            }
        }

        private fun handleSocketConnect() :Boolean{
            socket= Socket()
            val adr=InetSocketAddress(ip,bindPort)
            try {
                if (!socket.isConnected) {
                    socket.connect(adr)
                }else{
                    L.i(TAG, "handleSocketconnect: socket already connected")
                }
            } catch (e: Exception) {
                L.e(TAG, "handleSocketconnect: socket not connected address=${adr}, ${e.localizedMessage}")
                started.set(false)
                return false
            }

            L.i(TAG, "handleSocketconnect: conneted ")
            return true

        }

        private fun handleLogSendLoop() {
            try {
                socket.use {
                    outputStream= BufferedOutputStream(socket.getOutputStream())
                    outputStream.use {
                        var queueSize=outputBlockQueue.size
                        while (started.get() || (queueSize>0) ){
                            var data=outputBlockQueue.poll(Long.MAX_VALUE, TimeUnit.SECONDS)
                            if (data!=null) {
                                write(data)
                            }
                            while (outputBlockQueue.size>0){
                                val data=outputBlockQueue.poll(Long.MAX_VALUE, TimeUnit.SECONDS)
                                if (data!=null) {
                                    write(data)
                                }
                            }
                            outputStream?.flush()
                            L.i(TAG, "handleLogSendLoop#flush send: ")
                        }
                        outputStream=null
                        started.set(false)
                    }

                }
            } catch (e: Exception) {
                L.e(TAG, "handleLogSendLoop: socket oporation error : ${e.localizedMessage}")
            }
        }

        private fun write(data:ByteArray){
            outputStream?.write(data) ?: L.w(TAG, "write: outputStream is null")
        }

        fun started():Boolean{
            return started.get()
        }

        fun send(data:ByteArray){
            outputBlockQueue.offer(data,500,TimeUnit.MILLISECONDS)
        }

        fun flush(type:Int){
            outputStream?.flush()
        }

        fun stop(){
            if (started.get()) {
                L.i(TAG, "stop: client ")
                started.set(false)
//                outputStream=null
            }
        }
    }

    private var client:LogClientSocket?=null



    override fun println(item: LogItem) {
        val flattenedLog = flattener.flatten(item)
        realPrintln(flattenedLog)
    }

    override fun realPrintln(content: String) {
        if (client==null) {
            client = LogClientSocket()
        }
        client?.apply {
            if (!started()) {
                start()
            }
            val totalLength=content.length
            if (totalLength<maxChunkSize){
                send(SocketPacketUtil.pack(content))
            }else{
                var sStart=0
                var left=totalLength-sStart
                while (left>0){
                    val end=min(sStart+maxChunkSize,totalLength)
                    send(SocketPacketUtil.pack(content.substring(sStart,end )))
//                    L.v(TAG, { "realPrintln: split sStart=${sStart},end=${end},size=${end-sStart}" })
                    sStart+=maxChunkSize
                    left=totalLength-sStart
                }
            }
        }

    }

    override fun flush(type:Int) {
        client?.flush(type)
        client?.stop()
    }
}
package com.test.socket.localsocket

import com.langogo.lib.log.L
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

/**
 * 说明:
 * @author wangshengxing  08.01 2020
 */
class ClientSocket(val ip:String, val bindPort:Int=22612) {

    val  TAG = "ClientSocekt"

    private val started= AtomicBoolean(false)
    private val socket = Socket()
    private var outputStream: OutputStream? = null
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
            L.i(TAG, "start: conneted ")
            socket.use {
                outputStream=BufferedOutputStream(socket.getOutputStream())
                outputStream.use {
                    while (started.get()){
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
                        L.i(TAG, "flush send: ")
                    }

                }

            }
            L.i(TAG, "start: client socket closed ")

        }
    }

    private fun write(data:ByteArray){
        L.i(TAG, "write: ")
        outputStream?.write(data)
    }

    fun send(data:ByteArray){
        outputBlockQueue.add(data)
    }

    fun stop(){
        if (started.get()) {
            L.i(TAG, "stop: ")
            started.set(true)
            outputStream=null
        }
    }
}
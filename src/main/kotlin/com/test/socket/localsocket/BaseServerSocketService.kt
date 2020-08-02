package com.test.socket.localsocket

import com.langogo.lib.log.L
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 说明:
 * @author wangshengxing  08.02 2020
 */
abstract open class BaseServerSocketService(val bindPort:Int=22612){

    abstract val  TAG:String

    protected val started= AtomicBoolean(false)

    abstract fun start()
    fun stop(){
        if (started.get()) {
            L.i(TAG, "stop: ")
            started.set(true)
        }
    }
}
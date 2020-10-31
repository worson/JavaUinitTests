package com.test.socket.localsocket

import com.google.gson.JsonObject

/**
 * 说明:
 * @author wangshengxing  07.25 2020
 */
data class SocketRequest(val url:String,val method:String,val body:JsonObject) {

}
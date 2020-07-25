package com.test.socket.websocket

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  07.25 2020
 */
class ServerWebSocket(val listener: WebSocketListener) {

    private var mMockWebSocket: MockWebServer? = null

    init {
        mockWebSocket()
    }

    private fun mockWebSocket() {
        if (mMockWebSocket != null) {
            return
        }
        mMockWebSocket = MockWebServer()
        mMockWebSocket?.enqueue(MockResponse().withWebSocketUpgrade(listener))
    }

    fun getUrl():String{
        val hostName = mMockWebSocket?.getHostName()
        val port = mMockWebSocket?.getPort()
        val url = "ws:${hostName}:${port}"
        return url
    }


}
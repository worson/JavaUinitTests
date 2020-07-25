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
class TestMockWebSocket {
    var mMockWebSocket: MockWebServer? = null

    fun mockWebSocket() {
        if (mMockWebSocket != null) {
            return
        }
        mMockWebSocket = MockWebServer()
        mMockWebSocket?.enqueue(MockResponse().withWebSocketUpgrade(object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // 有客户端连接时回调
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                // 收到新消息时回调
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                // 客户端主动关闭时回调
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                // WebSocket 连接关闭
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                // 出错了
            }
        }))
    }

    fun getUrl():String{
        mockWebSocket()
        val hostName = mMockWebSocket?.getHostName()
        val port = mMockWebSocket?.getPort()
        val url = "ws:${hostName}:${port}"
        return url
    }

    @Test
    fun showUrl(){
        println("showUrl:${getUrl()}")
    }
}
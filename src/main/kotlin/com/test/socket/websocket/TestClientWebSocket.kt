package com.test.socket.websocket

import com.langogo.transcribe.comm.L
import com.langogo.transcribe.comm.log.LogConfiguration
import com.langogo.transcribe.comm.log.printer.ConsolePrinter
import okhttp3.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * 说明:
 * @author wangshengxing  07.25 2020
 */
class TestClientWebSocket {
    val TAG = "TestClientWebSocket"

    init {
        L.init(
            LogConfiguration.Builder()
                .addPrinter(ConsolePrinter())
                .build()
        )
    }

    @Test
    fun testReceiveServerHello() {
        L.i(TAG, { "testReceiveServerHello: " })
        var result=""
        val sever = ServerWebSocket(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                L.i(TAG, { "onOpen: server" })
                webSocket.send("hello")
            }
        })
        L.i(TAG, { "testReceiveServerHello: server info ${sever.getUrl()}" })
        val httpClient = OkHttpClient.Builder()
            .pingInterval(40, TimeUnit.SECONDS) // 设置 PING 帧发送间隔
            .build()
        val webSocketUrl = sever.getUrl()
        val request = Request.Builder()
            .url(webSocketUrl)
            .build()

        httpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                result=text
                L.i(TAG, { "onMessage: client $text" })
            }
        })
        Thread.sleep(2000)
        Assert.assertEquals("hello",result)
    }

}
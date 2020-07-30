package com.test.http

import com.langogo.lib.log.L
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert
import org.junit.Test
import java.net.InetSocketAddress
import java.net.Proxy


/**
 * 说明:
 * @author wangshengxing  07.26 2020
 */
class TestProxyRequest {
    val  TAG = "TestProxyRequest"
    @Test
    fun sampleHttpRequest(){
        val client=OkHttpClient()
        val request=Request.Builder().url("https://www.google.com.hk/").build()
        val response=client.newCall(request).execute()
        Assert.assertEquals(200,response.code())
        L.i(TAG, { "sampleHttpRequest: ${response.body()?.string()}" })
    }

    @Test
    fun sampleProxyHttpRequest(){
        val client=OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 1235)))
            .build()
        val request=Request.Builder().url("https://www.google.com.hk/").build()
        val response=client.newCall(request).execute()
        Assert.assertEquals(200,response.code())
        L.i(TAG, { "sampleHttpRequest: ${response.body()?.string()}" })
    }
}
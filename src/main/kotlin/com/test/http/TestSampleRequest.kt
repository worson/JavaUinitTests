package com.test.http

import com.langogo.transcribe.comm.L
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert
import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  07.26 2020
 */
class TestSampleRequest {
    val  TAG = "TestSampleRequest"
    @Test
    fun sampleHttpRequest(){
        val client=OkHttpClient()
        val request=Request.Builder().url("https://www.baidu.com/").build()
        val response=client.newCall(request).execute()
        Assert.assertEquals(200,response.code())
        L.i(TAG, { "sampleHttpRequest: ${response.body()?.string()}" })
    }
}
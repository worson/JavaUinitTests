package com.test.kotlin.reflect

import org.junit.Test

/**
 * 说明:
 * @author wangshengxing  07.30 2020
 */
class MethodReflectUnitTest {

    @Test
    fun testReflectStaticMethod(){
        val cls=Class.forName("java.lang.System")
        val out=cls?.getDeclaredField("out")
        val method=out?.type?.getMethod("println",Class.forName("java.lang.String"))
        println("method:${method}")
        method?.invoke(out.get(cls),"testReflectStaticMethod")
    }

    @Test
    fun testReflectCurrentTime(){
        val method=Class.forName("java.lang.System")?.getMethod("currentTimeMillis")
        println("method:${method}")
        println("method: result ${method?.invoke(null)}")
    }
}
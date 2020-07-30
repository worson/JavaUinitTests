package com.langogo.lib.log

/**
 * 说明:日志处理拦截器
 * @author wangshengxing  07.17 2020
 */
interface Interceptor {

    fun intercept(log: LogItem): LogItem

}
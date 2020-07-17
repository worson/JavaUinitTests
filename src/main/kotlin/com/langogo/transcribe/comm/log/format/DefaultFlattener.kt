package com.langogo.transcribe.comm.log.format

import com.langogo.transcribe.comm.log.LogLevel

/**
 * 说明:
 * @author wangshengxing  07.15 2020
 */
class DefaultFlattener:Flattener {

    override fun flatten(logLevel: Int, tag: String?, message: String?): String {
        return "$tag:$message"
    }

}
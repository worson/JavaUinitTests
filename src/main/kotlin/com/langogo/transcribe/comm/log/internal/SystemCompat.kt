package com.langogo.transcribe.comm.log.internal



/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
object SystemCompat {
    /**
     * The line separator of system.
     */
    var lineSeparator: String = Platform.get().lineSeparator()
}
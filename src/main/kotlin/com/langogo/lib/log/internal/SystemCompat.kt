package com.langogo.lib.log.internal



/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
object SystemCompat {
    /**
     * The line separator of system.
     */
    val lineSeparator: String = Platform.get()
        .lineSeparator()
}
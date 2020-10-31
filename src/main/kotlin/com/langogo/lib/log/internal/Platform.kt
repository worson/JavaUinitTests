package com.langogo.lib.log.internal

import com.langogo.lib.log.printer.AndroidPrinter

import com.langogo.lib.log.printer.ConsolePrinter
import com.langogo.lib.log.printer.Printer


/**
 * 说明:
 * @author wangshengxing  07.17 2020
 */
@Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
open class Platform {

    open fun lineSeparator(): String {
        return System.lineSeparator()
    }

    open fun defaultPrinter(): Printer {
        return ConsolePrinter()
    }

    open fun warn(msg: String) {
        println(msg)
    }

    internal class Android : Platform() {
        override fun lineSeparator(): String {
            return System.lineSeparator()
        }

        override fun defaultPrinter(): Printer {
            return AndroidPrinter()
        }

        override fun warn(msg: String) {
            println(msg)
        }
    }

    companion object {
        private val PLATFORM =
            findPlatform()
        fun get(): Platform {
            return PLATFORM
        }

        private fun findPlatform(): Platform {
            try {
                Class.forName("android.os.Build")
                return Android()
            } catch (ignored: ClassNotFoundException) {

            }
            return Platform()
        }
    }
}
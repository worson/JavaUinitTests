package com.langogo.lib.log

/**
 * 说明:
 *
 * @author wangshengxing  07.15 2020
 */
object LogLevel {

    const val VERBOSE = 2

    /**
     * Log level for d.
     */
    const val DEBUG = 3

    /**
     * Log level for i.
     */
    const val INFO = 4

    /**
     * Log level for w.
     */
    const val WARN = 5

    /**
     * Log level for e.
     */
    const val ERROR = 6

    /**
     * Log level for #init, printing all logs.
     */
    const val ALL = Int.MIN_VALUE

    /**
     * Log level for #init, printing no log.
     */
    const val NONE = Int.MAX_VALUE

    /**
     * Get a name representing the specified log level.
     *
     *
     * The returned name may be<br></br>
     * Level less than [LogLevel.VERBOSE]: "VERBOSE-N", N means levels below
     * [LogLevel.VERBOSE]<br></br>
     * [LogLevel.VERBOSE]: "VERBOSE"<br></br>
     * [LogLevel.DEBUG]: "DEBUG"<br></br>
     * [LogLevel.INFO]: "INFO"<br></br>
     * [LogLevel.WARN]: "WARN"<br></br>
     * [LogLevel.ERROR]: "ERROR"<br></br>
     * Level greater than [LogLevel.ERROR]: "ERROR+N", N means levels above
     * [LogLevel.ERROR]
     *
     * @param logLevel the log level to get name for
     * @return the name
     */
    fun getLevelName(logLevel: Int): String {
        val levelName: String
        levelName = when (logLevel) {
            VERBOSE -> "VERBOSE"
            DEBUG -> "DEBUG"
            INFO -> "INFO"
            WARN -> "WARN"
            ERROR -> "ERROR"
            else -> if (logLevel < VERBOSE) {
                "VERBOSE-" + (VERBOSE - logLevel)
            } else {
                "ERROR+" + (logLevel - ERROR)
            }
        }
        return levelName
    }

    /**
     * Get a short name representing the specified log level.
     *
     *
     * The returned name may be<br></br>
     * Level less than [LogLevel.VERBOSE]: "V-N", N means levels below
     * [LogLevel.VERBOSE]<br></br>
     * [LogLevel.VERBOSE]: "V"<br></br>
     * [LogLevel.DEBUG]: "D"<br></br>
     * [LogLevel.INFO]: "I"<br></br>
     * [LogLevel.WARN]: "W"<br></br>
     * [LogLevel.ERROR]: "E"<br></br>
     * Level greater than [LogLevel.ERROR]: "E+N", N means levels above
     * [LogLevel.ERROR]
     *
     * @param logLevel the log level to get short name for
     * @return the short name
     */
    fun getShortLevelName(logLevel: Int): String {
        val levelName: String
        levelName = when (logLevel) {
            VERBOSE -> "V"
            DEBUG -> "D"
            INFO -> "I"
            WARN -> "W"
            ERROR -> "E"
            else -> if (logLevel < VERBOSE) {
                "V-"
            } else {
                "E+"
            }
        }
        return levelName
    }
}
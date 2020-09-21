package com.skoumal.teanity.tools.log

import android.annotation.SuppressLint
import android.util.Log

class Taggable(@JvmField val tag: String)

@SuppressLint("LogConditional")
inline fun <reified T : Any> T.log(priority: Int, message: String?, throwable: Throwable? = null) {
    val tag = when (this) {
        is Taggable -> this.tag
        else -> this::class.java.simpleName
    } ?: "Logger"

    val messageBuilder = StringBuilder(message ?: "").also {
        if (throwable != null) {
            it.appendLine()
            it.append(throwable.stackTraceToString())
        }
    }

    Log.println(priority, tag, messageBuilder.toString())
}

inline fun <reified T : Any> T.verbose(message: String?, throwable: Throwable? = null) =
    log(Log.VERBOSE, message, throwable)

inline fun <reified T : Any> T.debug(message: String?, throwable: Throwable? = null) =
    log(Log.DEBUG, message, throwable)

inline fun <reified T : Any> T.info(message: String?, throwable: Throwable? = null) =
    log(Log.INFO, message, throwable)

inline fun <reified T : Any> T.warn(message: String?, throwable: Throwable? = null) =
    log(Log.WARN, message, throwable)

inline fun <reified T : Any> T.error(message: String?, throwable: Throwable? = null) =
    log(Log.ERROR, message, throwable)

inline fun <reified T : Any> T.error(throwable: Throwable) =
    error(null, throwable)

inline fun <reified T : Any> T.wtf(message: String?, throwable: Throwable? = null) =
    log(Log.ASSERT, message, throwable)
package com.srishti.ai

import android.util.Log

object AppLogger {
    private const val tag = "Srishti"

    fun info(message: String) {
        runCatching {
            Log.i(tag, message)
        }
    }

    fun error(message: String, throwable: Throwable? = null) {
        runCatching {
            Log.e(tag, message, throwable)
        }
    }
}

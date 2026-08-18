package com.srishti.ai

import android.util.Log

object AppLogger {
    private const val tag = "Srishti"

    fun info(message: String) {
        Log.i(tag, message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
}

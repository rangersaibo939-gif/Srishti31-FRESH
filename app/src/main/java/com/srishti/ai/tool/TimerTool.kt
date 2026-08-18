package com.srishti.ai.tool

import android.os.CountDownTimer
import com.srishti.ai.AppLogger
import com.srishti.ai.tool.risk.ToolRisk

class TimerTool(
    private val durationMillis: Long
) : Tool {
    private var activeTimer: CountDownTimer? = null

    override val name: String = "timer"
    override val description: String = "Starts a bounded local countdown timer."
    override val risk: ToolRisk = ToolRisk.SAFE

    @Synchronized
    override fun execute(): ToolResult {
        if (durationMillis !in MIN_DURATION_MILLIS..MAX_DURATION_MILLIS) {
            return ToolResult.failure(
                message = "Timer duration must be between 1 second and 24 hours.",
                error = "INVALID_TIMER_DURATION"
            )
        }
        if (activeTimer != null) {
            return ToolResult.failure(
                message = "A timer is already running.",
                error = "TIMER_ALREADY_RUNNING"
            )
        }

        return try {
            activeTimer = object : CountDownTimer(durationMillis, durationMillis) {
                override fun onTick(remainingMillis: Long) = Unit

                override fun onFinish() {
                    synchronized(this@TimerTool) {
                        activeTimer = null
                    }
                    AppLogger.info("Timer finished")
                }
            }.start()
            ToolResult.success("Timer started for ${durationMillis / 1000} seconds.")
        } catch (exception: Exception) {
            activeTimer = null
            ToolResult.failure("Unable to start timer.", exception.message)
        }
    }

    private companion object {
        const val MIN_DURATION_MILLIS = 1_000L
        const val MAX_DURATION_MILLIS = 24 * 60 * 60 * 1_000L
    }
}

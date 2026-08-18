package com.srishti.ai.tool

import android.content.Context
import android.provider.Settings
import com.srishti.ai.tool.risk.ToolRisk

class BrightnessTool(
    context: Context,
    private val targetBrightness: Int? = null
) : Tool {
    private val appContext = context.applicationContext

    override val name: String = if (targetBrightness == null) "brightness_read" else "brightness_set"
    override val description: String = if (targetBrightness == null) {
        "Reads the current screen brightness."
    } else {
        "Sets screen brightness to a validated level when permitted."
    }
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        return try {
            if (targetBrightness == null) {
                val currentBrightness = Settings.System.getInt(
                    appContext.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS
                )
                ToolResult.success("Screen brightness: $currentBrightness/$MAX_BRIGHTNESS")
            } else if (targetBrightness !in MIN_BRIGHTNESS..MAX_BRIGHTNESS) {
                ToolResult.failure(
                    message = "Brightness must be between $MIN_BRIGHTNESS and $MAX_BRIGHTNESS.",
                    error = "INVALID_BRIGHTNESS"
                )
            } else if (!Settings.System.canWrite(appContext)) {
                ToolResult.failure(
                    message = "Screen brightness cannot be changed without system-write access.",
                    error = "WRITE_SETTINGS_NOT_PERMITTED"
                )
            } else if (!Settings.System.putInt(
                    appContext.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS,
                    targetBrightness
                )
            ) {
                ToolResult.failure(
                    message = "Android rejected the screen brightness change.",
                    error = "BRIGHTNESS_WRITE_FAILED"
                )
            } else {
                ToolResult.success("Screen brightness set to $targetBrightness/$MAX_BRIGHTNESS")
            }
        } catch (exception: Exception) {
            ToolResult.failure("Unable to access screen brightness.", exception.message)
        }
    }

    private companion object {
        const val MIN_BRIGHTNESS = 0
        const val MAX_BRIGHTNESS = 255
    }
}

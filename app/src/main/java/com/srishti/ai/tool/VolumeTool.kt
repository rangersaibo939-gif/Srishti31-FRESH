package com.srishti.ai.tool

import android.content.Context
import android.media.AudioManager
import com.srishti.ai.tool.risk.ToolRisk

class VolumeTool(
    context: Context,
    private val targetVolume: Int? = null
) : Tool {
    private val audioManager = context.applicationContext.getSystemService(AudioManager::class.java)

    override val name: String = if (targetVolume == null) "volume_read" else "volume_set"
    override val description: String = if (targetVolume == null) {
        "Reads the current music volume."
    } else {
        "Sets the music volume to a validated level."
    }
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        val manager = audioManager ?: return ToolResult.failure(
            message = "Audio service is unavailable.",
            error = "AUDIO_SERVICE_UNAVAILABLE"
        )

        return try {
            val currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val maximumVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            if (targetVolume == null) {
                ToolResult.success("Music volume: $currentVolume/$maximumVolume")
            } else if (targetVolume !in 0..maximumVolume) {
                ToolResult.failure(
                    message = "Volume must be between 0 and $maximumVolume.",
                    error = "INVALID_VOLUME"
                )
            } else {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, 0)
                ToolResult.success("Music volume set to $targetVolume/$maximumVolume")
            }
        } catch (exception: Exception) {
            ToolResult.failure("Unable to access music volume.", exception.message)
        }
    }
}

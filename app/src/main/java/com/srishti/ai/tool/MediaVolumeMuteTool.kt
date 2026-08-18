package com.srishti.ai.tool

import android.content.Context
import android.media.AudioManager
import com.srishti.ai.tool.risk.ToolRisk

class MediaVolumeMuteTool(
    context: Context,
    private val mute: Boolean? = null
) : Tool {
    private val audioManager = context.applicationContext.getSystemService(AudioManager::class.java)

    override val name: String = when (mute) {
        null -> "media_volume_mute_status"
        true -> "media_volume_mute"
        false -> "media_volume_unmute"
    }
    override val description: String = when (mute) {
        null -> "Reads music-stream volume and mute state."
        true -> "Mutes only the music stream."
        false -> "Unmutes only the music stream."
    }
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        val manager = audioManager ?: return ToolResult.failure(
            message = "Audio service is unavailable.",
            error = "AUDIO_SERVICE_UNAVAILABLE"
        )

        return try {
            val currentVolume = manager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val muted = manager.isStreamMute(AudioManager.STREAM_MUSIC)
            if (mute == null) {
                ToolResult.success("Music volume: $currentVolume\nMusic muted: $muted")
            } else {
                manager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    if (mute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE,
                    0
                )
                ToolResult.success(if (mute) "Music stream muted." else "Music stream unmuted.")
            }
        } catch (exception: Exception) {
            ToolResult.failure("Unable to change music mute state.", exception.message)
        }
    }
}

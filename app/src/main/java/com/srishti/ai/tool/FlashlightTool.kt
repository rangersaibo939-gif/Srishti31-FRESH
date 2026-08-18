package com.srishti.ai.tool

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.srishti.ai.tool.risk.ToolRisk

class FlashlightTool(
    context: Context,
    private val enabled: Boolean
) : Tool {
    private val appContext = context.applicationContext

    override val name: String = if (enabled) "flashlight_on" else "flashlight_off"
    override val description: String = if (enabled) {
        "Turns on the device flashlight."
    } else {
        "Turns off the device flashlight."
    }
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        return try {
            val packageManager = appContext.packageManager
            if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                return ToolResult.failure(
                    message = "This device does not have a usable camera flash.",
                    error = "FLASH_NOT_AVAILABLE"
                )
            }

            val cameraManager = appContext.getSystemService(CameraManager::class.java)
                ?: return ToolResult.failure(
                    message = "Camera service is unavailable.",
                    error = "CAMERA_SERVICE_UNAVAILABLE"
                )
            val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: return ToolResult.failure(
                message = "This device does not have a usable camera flash.",
                error = "FLASH_NOT_AVAILABLE"
            )

            cameraManager.setTorchMode(cameraId, enabled)
            ToolResult.success(if (enabled) "Flashlight turned on." else "Flashlight turned off.")
        } catch (exception: Exception) {
            ToolResult.failure("Unable to change flashlight state.", exception.message)
        }
    }
}

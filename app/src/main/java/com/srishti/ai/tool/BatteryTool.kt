package com.srishti.ai.tool

import android.content.Context
import android.os.BatteryManager
import com.srishti.ai.tool.risk.ToolRisk

class BatteryTool(context: Context) : Tool {
    private val appContext = context.applicationContext

    override val name: String = "battery"
    override val description: String = "Reads battery percentage and charging state."
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        return try {
            val batteryManager = appContext.getSystemService(BatteryManager::class.java)
                ?: return ToolResult.failure(
                    message = "Battery service is unavailable.",
                    error = "BATTERY_SERVICE_UNAVAILABLE"
                )
            val percentage = batteryManager
                .getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                .takeIf { it in 0..100 }
                ?.toString()
                ?: "unknown"
            ToolResult.success("Battery: $percentage%\nCharging: ${batteryManager.isCharging}")
        } catch (exception: Exception) {
            ToolResult.failure("Unable to read battery state.", exception.message)
        }
    }
}

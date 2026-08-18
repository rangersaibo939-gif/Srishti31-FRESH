package com.srishti.ai.tool

import android.content.Context
import android.os.BatteryManager
import android.os.Build
import com.srishti.ai.tool.risk.ToolRisk

class DeviceInfoTool(
    private val context: Context
) : Tool {
    override val name: String = "device_info"
    override val description: String = "Reads basic local device information."
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        val batteryManager = context.getSystemService(BatteryManager::class.java)
        val batteryPercentage = batteryManager
            ?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            ?.takeIf { it >= 0 }
            ?.toString()
            ?: "unknown"

        return ToolResult.success(
            message = "Battery: $batteryPercentage%\nAndroid: ${Build.VERSION.RELEASE}"
        )
    }
}

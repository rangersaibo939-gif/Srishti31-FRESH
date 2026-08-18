package com.srishti.ai.tool

import android.os.Environment
import android.os.StatFs
import com.srishti.ai.tool.risk.ToolRisk

class DeviceStorageTool : Tool {
    override val name: String = "device_storage"
    override val description: String = "Reads available and total internal storage."
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        return try {
            val stats = StatFs(Environment.getDataDirectory().path)
            ToolResult.success(
                "Internal storage available: ${stats.availableBytes} bytes\n" +
                    "Internal storage total: ${stats.totalBytes} bytes"
            )
        } catch (exception: Exception) {
            ToolResult.failure("Unable to read internal storage.", exception.message)
        }
    }
}

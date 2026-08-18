package com.srishti.ai.tool

import com.srishti.ai.tool.risk.ToolRisk
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentTimeTool : Tool {
    override val name: String = "current_time"
    override val description: String = "Reads the current local date and time."
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        return try {
            val now = Date()
            val localTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(now)
            val localDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now)
            ToolResult.success("Local date: $localDate\nLocal time: $localTime")
        } catch (exception: Exception) {
            ToolResult.failure("Unable to read local date and time.", exception.message)
        }
    }
}

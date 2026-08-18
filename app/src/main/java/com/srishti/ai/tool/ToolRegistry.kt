package com.srishti.ai.tool

import com.srishti.ai.AppLogger
import com.srishti.ai.tool.risk.ToolRisk

class ToolRegistry {
    private val tools = LinkedHashMap<String, Tool>()

    @Synchronized
    fun register(tool: Tool): Boolean {
        if (tool.name.isBlank() || tools.containsKey(tool.name)) {
            AppLogger.error("Rejected tool registration: ${tool.name}")
            return false
        }
        tools[tool.name] = tool
        return true
    }

    @Synchronized
    fun find(name: String): Tool? = tools[name]

    @Synchronized
    fun list(): List<Tool> = tools.values.toList()

    @Synchronized
    fun execute(name: String): ToolResult {
        val tool = find(name) ?: return ToolResult.failure(
            message = "Tool not found: $name",
            error = "UNKNOWN_TOOL"
        )

        if (tool.risk != ToolRisk.SAFE) {
            return ToolResult.failure(
                message = "Tool requires a safety decision before execution: $name",
                error = "TOOL_RISK_${tool.risk.name}"
            )
        }

        return try {
            tool.execute()
        } catch (exception: Exception) {
            AppLogger.error("Tool execution failed: $name", exception)
            ToolResult.failure("Tool execution failed: $name", exception.message)
        }
    }
}

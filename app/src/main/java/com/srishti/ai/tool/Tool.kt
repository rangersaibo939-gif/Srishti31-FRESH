package com.srishti.ai.tool

import com.srishti.ai.tool.risk.ToolRisk

interface Tool {
    val name: String
    val description: String
    val risk: ToolRisk

    fun execute(): ToolResult
}

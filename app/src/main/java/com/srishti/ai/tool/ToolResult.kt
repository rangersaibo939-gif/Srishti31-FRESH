package com.srishti.ai.tool

data class ToolResult(
    val success: Boolean,
    val message: String,
    val error: String? = null
) {
    companion object {
        fun success(message: String): ToolResult = ToolResult(true, message)

        fun failure(message: String, error: String? = null): ToolResult =
            ToolResult(false, message, error)
    }
}

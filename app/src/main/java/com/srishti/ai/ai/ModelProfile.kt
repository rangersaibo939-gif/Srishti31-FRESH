package com.srishti.ai.ai

data class ModelProfile(
    val id: String,
    val provider: String,
    val capabilities: Set<TaskType>,
    val priority: Int = 100,
    val enabled: Boolean = true
)

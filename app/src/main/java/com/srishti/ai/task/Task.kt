package com.srishti.ai.task

data class Task(
    val id: String,
    val conversationId: String,
    val description: String,
    val state: TaskState,
    val createdAt: Long,
    val updatedAt: Long
)

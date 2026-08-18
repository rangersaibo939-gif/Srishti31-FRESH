package com.srishti.ai.task

enum class TaskState {
    CREATED,
    ANALYZING,
    PLANNED,
    EXECUTING,
    VERIFYING,
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT,
    BLOCKED,
    RECOVERY_REQUIRED
}

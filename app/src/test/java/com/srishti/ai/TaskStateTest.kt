package com.srishti.ai

import com.srishti.ai.task.Task
import com.srishti.ai.task.TaskState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class TaskStateTest {

    @Test
    fun taskStoresAllFields() {
        val task = Task(
            id = "task-1",
            conversationId = "conversation-1",
            description = "Run a research task",
            state = TaskState.CREATED,
            createdAt = 100L,
            updatedAt = 100L
        )

        assertEquals("task-1", task.id)
        assertEquals("conversation-1", task.conversationId)
        assertEquals("Run a research task", task.description)
        assertEquals(TaskState.CREATED, task.state)
        assertEquals(100L, task.createdAt)
        assertEquals(100L, task.updatedAt)
    }

    @Test
    fun taskCopyCanTransitionState() {
        val original = Task(
            id = "task-1",
            conversationId = "conversation-1",
            description = "Do work",
            state = TaskState.CREATED,
            createdAt = 100L,
            updatedAt = 100L
        )

        val updated = original.copy(
            state = TaskState.ANALYZING,
            updatedAt = 200L
        )

        assertEquals(TaskState.CREATED, original.state)
        assertEquals(TaskState.ANALYZING, updated.state)
        assertEquals(100L, original.updatedAt)
        assertEquals(200L, updated.updatedAt)
        assertNotEquals(original, updated)
    }

    @Test
    fun taskStateContainsExpectedLifecycleStates() {
        assertEquals(
            listOf(
                "CREATED",
                "ANALYZING",
                "PLANNED",
                "EXECUTING",
                "VERIFYING",
                "COMPLETED",
                "FAILED",
                "CANCELLED",
                "TIMEOUT",
                "BLOCKED",
                "RECOVERY_REQUIRED"
            ),
            TaskState.entries.map { it.name }
        )
    }
}

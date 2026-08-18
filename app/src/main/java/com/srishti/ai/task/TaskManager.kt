package com.srishti.ai.task

import android.content.Context
import com.srishti.ai.AppLogger
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class TaskManager(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Synchronized
    fun createTask(conversationId: String, description: String): Task {
        val now = System.currentTimeMillis()
        val task = Task(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            description = description,
            state = TaskState.CREATED,
            createdAt = now,
            updatedAt = now
        )
        persist(readTasks() + task)
        return task
    }

    @Synchronized
    fun getTask(taskId: String): Task? = readTasks().firstOrNull { it.id == taskId }

    @Synchronized
    fun getTasks(conversationId: String? = null): List<Task> {
        val tasks = readTasks()
        return if (conversationId == null) tasks else tasks.filter { it.conversationId == conversationId }
    }

    @Synchronized
    fun transition(taskId: String, nextState: TaskState): Task? {
        val tasks = readTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index < 0) return null

        val currentTask = tasks[index]
        if (nextState !in allowedTransitions[currentTask.state].orEmpty()) {
            AppLogger.error("Invalid task transition: ${currentTask.state} to $nextState")
            return currentTask
        }

        val updatedTask = currentTask.copy(state = nextState, updatedAt = System.currentTimeMillis())
        tasks[index] = updatedTask
        persist(tasks)
        return updatedTask
    }

    private fun readTasks(): List<Task> {
        val serialized = preferences.getString(KEY_TASKS, null) ?: return emptyList()
        return try {
            val tasks = JSONArray(serialized)
            buildList {
                for (index in 0 until tasks.length()) {
                    val item = tasks.optJSONObject(index) ?: continue
                    val id = item.optString(KEY_ID)
                    val conversationId = item.optString(KEY_CONVERSATION_ID)
                    val description = item.optString(KEY_DESCRIPTION)
                    val stateName = item.optString(KEY_STATE)
                    val createdAt = item.optLong(KEY_CREATED_AT)
                    val updatedAt = item.optLong(KEY_UPDATED_AT)
                    val state = stateName.toTaskStateOrNull()
                    if (id.isNotEmpty() && conversationId.isNotEmpty() && description.isNotEmpty() &&
                        state != null && createdAt > 0L && updatedAt > 0L
                    ) {
                        add(Task(id, conversationId, description, state, createdAt, updatedAt))
                    }
                }
            }
        } catch (exception: Exception) {
            AppLogger.error("Unable to read tasks", exception)
            emptyList()
        }
    }

    private fun persist(tasks: List<Task>) {
        try {
            val serialized = JSONArray().apply {
                tasks.forEach { task ->
                    put(JSONObject().apply {
                        put(KEY_ID, task.id)
                        put(KEY_CONVERSATION_ID, task.conversationId)
                        put(KEY_DESCRIPTION, task.description)
                        put(KEY_STATE, task.state.name)
                        put(KEY_CREATED_AT, task.createdAt)
                        put(KEY_UPDATED_AT, task.updatedAt)
                    })
                }
            }
            val committed = preferences.edit().putString(KEY_TASKS, serialized.toString()).commit()
            if (!committed) AppLogger.error("Unable to commit task state")
        } catch (exception: Exception) {
            AppLogger.error("Unable to persist tasks", exception)
        }
    }

    private fun String.toTaskStateOrNull(): TaskState? = try {
        TaskState.valueOf(this)
    } catch (_: IllegalArgumentException) {
        null
    }

    private companion object {
        val allowedTransitions = mapOf(
            TaskState.CREATED to setOf(TaskState.ANALYZING, TaskState.CANCELLED, TaskState.TIMEOUT, TaskState.BLOCKED),
            TaskState.ANALYZING to setOf(TaskState.PLANNED, TaskState.COMPLETED, TaskState.FAILED, TaskState.CANCELLED, TaskState.TIMEOUT, TaskState.BLOCKED),
            TaskState.PLANNED to setOf(TaskState.EXECUTING, TaskState.FAILED, TaskState.CANCELLED, TaskState.TIMEOUT, TaskState.BLOCKED),
            TaskState.EXECUTING to setOf(TaskState.VERIFYING, TaskState.FAILED, TaskState.CANCELLED, TaskState.TIMEOUT, TaskState.BLOCKED),
            TaskState.VERIFYING to setOf(TaskState.COMPLETED, TaskState.FAILED, TaskState.CANCELLED, TaskState.TIMEOUT, TaskState.BLOCKED, TaskState.RECOVERY_REQUIRED),
            TaskState.RECOVERY_REQUIRED to setOf(TaskState.ANALYZING, TaskState.FAILED, TaskState.CANCELLED),
            TaskState.COMPLETED to emptySet(),
            TaskState.FAILED to emptySet(),
            TaskState.CANCELLED to emptySet(),
            TaskState.TIMEOUT to emptySet(),
            TaskState.BLOCKED to emptySet()
        )
        const val PREFERENCES_NAME = "srishti_tasks"
        const val KEY_TASKS = "tasks"
        const val KEY_ID = "id"
        const val KEY_CONVERSATION_ID = "conversationId"
        const val KEY_DESCRIPTION = "description"
        const val KEY_STATE = "state"
        const val KEY_CREATED_AT = "createdAt"
        const val KEY_UPDATED_AT = "updatedAt"
    }
}

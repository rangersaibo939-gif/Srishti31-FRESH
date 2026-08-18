package com.srishti.ai

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.srishti.ai.memory.MemoryEngine
import com.srishti.ai.task.TaskManager
import com.srishti.ai.task.TaskState

class SrishtiViewModel(
    private val personalityEngine: PersonalityEngine = PersonalityEngine(),
    private val memoryEngine: MemoryEngine? = null,
    private val taskManager: TaskManager? = null
) {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()
    private var nextMessageId = 1L

    fun updateDraft(value: String) {
        _state.value = _state.value.copy(draft = value)
    }

    fun sendMessage() {
        val text = _state.value.draft.trim()
        if (text.isEmpty()) return

        memoryEngine?.store(text)
        val task = taskManager?.createTask(_state.value.conversationId, text)
        task?.let { taskManager?.transition(it.id, TaskState.ANALYZING) }
        val userMessage = SrishtiMessage(nextMessageId++, text, fromSrishti = false)
        val response = personalityEngine.createResponse(text)
        val assistantMessage = SrishtiMessage(nextMessageId++, response, fromSrishti = true)
        _state.value = _state.value.copy(
            messages = _state.value.messages + userMessage + assistantMessage,
            draft = ""
        )
        task?.let { taskManager?.transition(it.id, TaskState.COMPLETED) }
        AppLogger.info("Local response generated")
    }
}

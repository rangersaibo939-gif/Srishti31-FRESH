package com.srishti.ai

data class SrishtiMessage(
    val id: Long,
    val text: String,
    val fromSrishti: Boolean
)

data class AppState(
    val conversationId: String = "main-conversation",
    val messages: List<SrishtiMessage> = listOf(
        SrishtiMessage(
            id = 0L,
            text = SrishtiPersonality.greeting,
            fromSrishti = true
        )
    ),
    val draft: String = "",
    val status: String = "LOCAL CORE ONLINE",
    val isTyping: Boolean = false
)

package com.srishti.ai.ai

import com.srishti.ai.SrishtiPersonality

class LocalAiProvider : AiProvider {
    override fun generateResponse(input: String): String {
        val message = input.trim().lowercase()
        return when {
            message.contains("hello") || message.contains("hi") || message.contains("hey") ->
                SrishtiPersonality.greeting
            message.contains("how are you") ->
                "All systems are steady and present. Thank you for asking."
            message.contains("thank") ->
                "You’re welcome. I’m right here whenever you need a thought partner."
            else ->
                "I’m listening. That sounds worth exploring together — what part should we start with?"
        }
    }
}

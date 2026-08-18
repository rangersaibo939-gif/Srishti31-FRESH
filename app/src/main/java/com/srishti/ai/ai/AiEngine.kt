package com.srishti.ai.ai

class AiEngine(
    private val provider: AiProvider = LocalAiProvider()
) {
    fun respond(input: String): String = provider.generateResponse(input)
}

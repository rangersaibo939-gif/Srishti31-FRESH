package com.srishti.ai

import com.srishti.ai.ai.AiEngine

class SrishtiCore(
    private val aiEngine: AiEngine = AiEngine()
) {
    fun respond(input: String): String = aiEngine.respond(input)
}

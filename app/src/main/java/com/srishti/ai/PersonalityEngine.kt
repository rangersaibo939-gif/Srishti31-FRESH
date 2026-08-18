package com.srishti.ai

class PersonalityEngine(
    private val core: SrishtiCore = SrishtiCore()
) {
    fun createResponse(input: String): String = core.respond(input)
}

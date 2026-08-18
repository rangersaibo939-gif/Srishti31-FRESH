package com.srishti.ai.ai

class ModelRouter(
    private val registry: ModelRegistry = ModelRegistry()
) {
    fun classify(input: String): TaskType {
        val text = input.lowercase()
        return when {
            text.containsAny("code", "coding", "program", "debug", "kotlin", "java", "python", "gradle", "github") -> TaskType.CODING
            text.containsAny("research", "search the web", "latest", "news", "find sources", "deep research") -> TaskType.RESEARCH
            text.containsAny("image", "picture", "photo", "draw", "generate an image") -> TaskType.IMAGE_GENERATION
            text.containsAny("look at", "what is in this image", "vision", "screenshot") -> TaskType.VISION
            else -> TaskType.GENERAL
        }
    }

    fun route(input: String): ModelProfile? = registry.candidates(classify(input)).firstOrNull()

    private fun String.containsAny(vararg terms: String): Boolean = terms.any(::contains)
}

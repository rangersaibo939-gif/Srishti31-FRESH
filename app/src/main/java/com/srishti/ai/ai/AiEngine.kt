package com.srishti.ai.ai

class AiEngine(
    private val localProvider: AiProvider = LocalAiProvider(),
    private val router: ModelRouter = ModelRouter()
) {
    private val fallbackEngine = FallbackEngine(
        providers = mapOf("local" to localProvider),
        router = router
    )

    fun classify(input: String): TaskType = router.classify(input)

    fun selectedModel(input: String): ModelProfile? = router.route(input)

    fun respond(input: String): String = fallbackEngine.respond(input)
}

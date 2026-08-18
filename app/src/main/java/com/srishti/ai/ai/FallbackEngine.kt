package com.srishti.ai.ai

class FallbackEngine(
    private val providers: Map<String, AiProvider>,
    private val router: ModelRouter = ModelRouter()
) {
    fun respond(input: String): String {
        val selected = router.route(input)
        val ordered = buildList {
            selected?.let { add(it) }
            addAll(ModelRegistry().candidates(router.classify(input)).filterNot { it.id == selected?.id })
        }

        var lastFailure: Throwable? = null
        for (model in ordered) {
            val provider = providers[model.provider] ?: continue
            try {
                return provider.generateResponse(input)
            } catch (t: Throwable) {
                lastFailure = t
            }
        }

        throw IllegalStateException("No AI provider available for ${router.classify(input)}", lastFailure)
    }
}

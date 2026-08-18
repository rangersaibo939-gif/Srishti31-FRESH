package com.srishti.ai.ai

class ModelRegistry(
    profiles: List<ModelProfile> = defaultProfiles()
) {
    private val models = profiles.toMutableList()

    fun register(profile: ModelProfile) {
        models.removeAll { it.id == profile.id }
        models += profile
    }

    fun candidates(task: TaskType): List<ModelProfile> = models
        .asSequence()
        .filter { it.enabled && task in it.capabilities }
        .sortedBy { it.priority }
        .toList()

    companion object {
        fun defaultProfiles(): List<ModelProfile> = listOf(
            ModelProfile("local", "local", TaskType.values().toSet(), priority = 100)
        )
    }
}

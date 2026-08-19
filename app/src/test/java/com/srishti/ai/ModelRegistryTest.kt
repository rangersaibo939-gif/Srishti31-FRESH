package com.srishti.ai

import com.srishti.ai.ai.ModelProfile
import com.srishti.ai.ai.ModelRegistry
import com.srishti.ai.ai.TaskType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelRegistryTest {

    @Test
    fun filtersModelsByCapability() {
        val registry = ModelRegistry(
            listOf(
                ModelProfile("coding", "provider", setOf(TaskType.CODING)),
                ModelProfile("research", "provider", setOf(TaskType.RESEARCH))
            )
        )

        assertEquals(
            listOf("coding"),
            registry.candidates(TaskType.CODING).map { it.id }
        )
    }

    @Test
    fun ignoresDisabledModels() {
        val registry = ModelRegistry(
            listOf(
                ModelProfile(
                    "disabled",
                    "provider",
                    setOf(TaskType.GENERAL),
                    enabled = false
                )
            )
        )

        assertTrue(registry.candidates(TaskType.GENERAL).isEmpty())
    }

    @Test
    fun sortsCandidatesByPriority() {
        val registry = ModelRegistry(
            listOf(
                ModelProfile("slow", "provider", setOf(TaskType.GENERAL), priority = 200),
                ModelProfile("fast", "provider", setOf(TaskType.GENERAL), priority = 10)
            )
        )

        assertEquals(
            listOf("fast", "slow"),
            registry.candidates(TaskType.GENERAL).map { it.id }
        )
    }

    @Test
    fun registerReplacesExistingModelWithSameId() {
        val registry = ModelRegistry(
            listOf(
                ModelProfile("local", "old", setOf(TaskType.GENERAL))
            )
        )

        registry.register(
            ModelProfile("local", "new", setOf(TaskType.CODING))
        )

        assertFalse(registry.candidates(TaskType.GENERAL).any { it.provider == "old" })
        assertTrue(registry.candidates(TaskType.CODING).any { it.provider == "new" })
    }
}

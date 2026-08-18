package com.srishti.ai.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ModelRouterTest {
    private val router = ModelRouter()

    @Test
    fun classifiesCodingRequests() {
        assertEquals(TaskType.CODING, router.classify("Help me debug this Kotlin Gradle build"))
    }

    @Test
    fun classifiesResearchRequests() {
        assertEquals(TaskType.RESEARCH, router.classify("Find the latest research sources"))
    }

    @Test
    fun classifiesVisionRequests() {
        assertEquals(TaskType.VISION, router.classify("Look at this screenshot"))
    }

    @Test
    fun routesKnownTaskToModel() {
        assertNotNull(router.route("Write Kotlin code"))
    }
}

package com.srishti.ai

import com.srishti.ai.ai.ModelRouter
import com.srishti.ai.ai.TaskType
import org.junit.Assert.assertEquals
import org.junit.Test

class ModelRouterTest {

    private val router = ModelRouter()

    @Test
    fun classifiesCodingRequests() {
        assertEquals(TaskType.CODING, router.classify("Debug this Kotlin code"))
    }

    @Test
    fun classifiesResearchRequests() {
        assertEquals(TaskType.RESEARCH, router.classify("Research the latest news"))
    }

    @Test
    fun classifiesImageRequests() {
        assertEquals(TaskType.IMAGE_GENERATION, router.classify("Generate an image"))
    }

    @Test
    fun classifiesVisionRequests() {
        assertEquals(TaskType.VISION, router.classify("Look at this screenshot"))
    }

    @Test
    fun defaultsToGeneral() {
        assertEquals(TaskType.GENERAL, router.classify("Hello Srishti"))
    }
}

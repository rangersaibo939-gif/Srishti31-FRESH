package com.srishti.ai

import com.srishti.ai.ai.AiEngine
import com.srishti.ai.ai.AiProvider
import com.srishti.ai.ai.TaskType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AiEngineTest {

    @Test
    fun classifiesThroughRouter() {
        val engine = AiEngine()

        assertEquals(
            TaskType.CODING,
            engine.classify("Debug this Kotlin code")
        )
    }

    @Test
    fun selectsModelForRequest() {
        val engine = AiEngine()

        assertNotNull(engine.selectedModel("Research the latest news"))
    }

    @Test
    fun respondsThroughConfiguredProvider() {
        val provider = object : AiProvider {
            override fun generateResponse(input: String): String {
                return "AI:$input"
            }
        }

        val engine = AiEngine(localProvider = provider)

        assertEquals("AI:Hello Srishti", engine.respond("Hello Srishti"))
    }
}

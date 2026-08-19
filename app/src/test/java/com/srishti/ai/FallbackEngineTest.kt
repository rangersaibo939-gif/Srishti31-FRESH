package com.srishti.ai

import com.srishti.ai.ai.AiProvider
import com.srishti.ai.ai.FallbackEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class FallbackEngineTest {

    @Test
    fun returnsProviderResponse() {
        val provider = object : AiProvider {
            override fun generateResponse(input: String): String = "response:$input"
        }

        val engine = FallbackEngine(
            providers = mapOf("local" to provider)
        )

        assertEquals("response:hello", engine.respond("hello"))
    }

    @Test
    fun throwsWhenNoProviderIsAvailable() {
        val engine = FallbackEngine(
            providers = emptyMap()
        )

        try {
            engine.respond("hello")
            fail("Expected IllegalStateException")
        } catch (expected: IllegalStateException) {
            // Expected.
        }
    }
}

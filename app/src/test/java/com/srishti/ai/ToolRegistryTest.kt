package com.srishti.ai

import com.srishti.ai.tool.Tool
import com.srishti.ai.tool.ToolRegistry
import com.srishti.ai.tool.ToolResult
import com.srishti.ai.tool.risk.ToolRisk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ToolRegistryTest {

    @Test
    fun registersAndFindsSafeTool() {
        val registry = ToolRegistry()
        val tool = fakeTool("hello", ToolRisk.SAFE)

        assertTrue(registry.register(tool))
        assertEquals(tool, registry.find("hello"))
        assertEquals(listOf("hello"), registry.list().map { it.name })
    }

    @Test
    fun rejectsDuplicateToolNames() {
        val registry = ToolRegistry()

        assertTrue(registry.register(fakeTool("hello", ToolRisk.SAFE)))
        assertFalse(registry.register(fakeTool("hello", ToolRisk.SAFE)))
    }

    @Test
    fun rejectsBlankToolNames() {
        val registry = ToolRegistry()

        assertFalse(registry.register(fakeTool("", ToolRisk.SAFE)))
        assertTrue(registry.list().isEmpty())
    }

    @Test
    fun unknownToolReturnsFailure() {
        val result = ToolRegistry().execute("missing")

        assertFalse(result.success)
        assertEquals("UNKNOWN_TOOL", result.error)
    }

    @Test
    fun riskyToolDoesNotExecute() {
        val registry = ToolRegistry()
        var executed = false

        registry.register(object : Tool {
            override val name = "danger"
            override val description = "test"
            override val risk = ToolRisk.CONFIRM

            override fun execute(): ToolResult {
                executed = true
                return ToolResult.success("should not run")
            }
        })

        val result = registry.execute("danger")

        assertFalse(result.success)
        assertFalse(executed)
        assertEquals("TOOL_RISK_CONFIRM", result.error)
    }

    @Test
    fun safeToolExecutionReturnsResult() {
        val registry = ToolRegistry()

        registry.register(object : Tool {
            override val name = "hello"
            override val description = "test"
            override val risk = ToolRisk.SAFE

            override fun execute(): ToolResult =
                ToolResult.success("hello world")
        })

        val result = registry.execute("hello")

        assertTrue(result.success)
        assertEquals("hello world", result.message)
    }

    @Test
    fun toolExceptionBecomesFailure() {
        val registry = ToolRegistry()

        registry.register(object : Tool {
            override val name = "broken"
            override val description = "test"
            override val risk = ToolRisk.SAFE

            override fun execute(): ToolResult {
                throw IllegalStateException("boom")
            }
        })

        val result = registry.execute("broken")

        assertFalse(result.success)
        assertEquals("Tool execution failed: broken", result.message)
        assertEquals("boom", result.error)
    }

    private fun fakeTool(name: String, risk: ToolRisk): Tool =
        object : Tool {
            override val name = name
            override val description = "test"
            override val risk = risk

            override fun execute(): ToolResult =
                ToolResult.success("ok")
        }
}

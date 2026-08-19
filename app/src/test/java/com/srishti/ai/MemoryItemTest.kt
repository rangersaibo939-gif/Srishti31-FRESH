package com.srishti.ai

import com.srishti.ai.memory.MemoryItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class MemoryItemTest {

    @Test
    fun storesAllMemoryFields() {
        val memory = MemoryItem(
            id = "memory-1",
            content = "User prefers concise answers.",
            timestamp = 123456789L
        )

        assertEquals("memory-1", memory.id)
        assertEquals("User prefers concise answers.", memory.content)
        assertEquals(123456789L, memory.timestamp)
    }

    @Test
    fun copyCanUpdateMemoryContent() {
        val original = MemoryItem(
            id = "memory-1",
            content = "Original memory",
            timestamp = 100L
        )

        val updated = original.copy(
            content = "Updated memory",
            timestamp = 200L
        )

        assertEquals("Original memory", original.content)
        assertEquals(100L, original.timestamp)
        assertEquals("Updated memory", updated.content)
        assertEquals(200L, updated.timestamp)
        assertNotEquals(original, updated)
    }

    @Test
    fun equalMemoryItemsCompareByValue() {
        val first = MemoryItem("memory-1", "Hello", 100L)
        val second = MemoryItem("memory-1", "Hello", 100L)

        assertEquals(first, second)
        assertEquals(first.hashCode(), second.hashCode())
    }
}

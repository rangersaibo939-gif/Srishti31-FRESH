package com.srishti.ai

import com.srishti.ai.tool.CurrentTimeTool
import com.srishti.ai.tool.risk.ToolRisk
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentTimeToolTest {

    private val tool = CurrentTimeTool()

    @Test
    fun hasExpectedIdentityAndRisk() {
        assertTrue(tool.name == "current_time")
        assertTrue(tool.risk == ToolRisk.SAFE)
    }

    @Test
    fun returnsCurrentDateAndTime() {
        val result = tool.execute()

        assertTrue(result.success)
        assertTrue(result.message.startsWith("Local date: "))

        val lines = result.message.lines()
        assertTrue(lines.size == 2)
        assertTrue(lines[1].startsWith("Local time: "))

        val expectedDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val expectedTimePattern = Regex("""\d{2}:\d{2}:\d{2}""")

        assertTrue(lines[0] == "Local date: $expectedDate")
        assertTrue(expectedTimePattern.matches(lines[1].removePrefix("Local time: ")))
    }
}

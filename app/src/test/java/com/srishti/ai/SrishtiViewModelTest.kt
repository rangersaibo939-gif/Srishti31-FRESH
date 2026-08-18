package com.srishti.ai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SrishtiViewModelTest {
    @Test
    fun sendMessage_addsUserAndAssistantMessages_andClearsDraft() {
        val viewModel = SrishtiViewModel()
        val before = viewModel.state.value.messages.size

        viewModel.updateDraft("hello Srishti")
        viewModel.sendMessage()

        val state = viewModel.state.value
        assertEquals(before + 2, state.messages.size)
        assertEquals("", state.draft)
        assertEquals("hello Srishti", state.messages[before].text)
        assertTrue(state.messages[before + 1].fromSrishti)
    }

    @Test
    fun sendMessage_ignores_blank_draft() {
        val viewModel = SrishtiViewModel()
        val before = viewModel.state.value

        viewModel.updateDraft("   ")
        viewModel.sendMessage()

        assertEquals(before, viewModel.state.value)
    }
}

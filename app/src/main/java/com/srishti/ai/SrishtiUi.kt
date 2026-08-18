package com.srishti.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Void = Color(0xFF080B12)
private val Panel = Color(0xFF111722)
private val Cyan = Color(0xFF5CE1E6)
private val SoftText = Color(0xFFA7B4C8)

@Composable
fun SrishtiApp(viewModel: SrishtiViewModel) {
    val state by viewModel.state.collectAsState()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Void) {
            SrishtiScreen(state, viewModel::updateDraft, viewModel::sendMessage)
        }
    }
}

@Composable
private fun SrishtiScreen(
    state: AppState,
    onDraftChanged: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Cyan.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text("S", color = Cyan, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(SrishtiPersonality.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(7.dp).clip(CircleShape).background(Cyan))
                    Spacer(Modifier.width(6.dp))
                    Text(state.status, color = Cyan, fontSize = 10.sp, letterSpacing = 1.2.sp)
                }
            }
            IconButton(onClick = { AppLogger.info("Settings placeholder tapped") }) {
                Text("⋯", color = SoftText, fontSize = 26.sp)
            }
        }

        Text("A quiet space for bright ideas.", color = SoftText, fontSize = 14.sp)

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            items(state.messages, key = { it.id }) { message ->
                MessageBubble(message)
            }
        }

        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { AppLogger.info("Microphone placeholder tapped") },
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(18.dp)).background(Panel)
            ) {
                Text("⌁", color = Cyan, fontSize = 24.sp)
            }
            OutlinedTextField(
                value = state.draft,
                onValueChange = onDraftChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text(SrishtiPersonality.emptyPrompt, color = SoftText.copy(alpha = 0.65f)) },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Cyan,
                    unfocusedBorderColor = Color(0xFF293342),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Cyan
                ),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(onSend = { onSend() })
            )
            Button(
                onClick = onSend,
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(18.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Cyan, contentColor = Void)
            ) {
                Text("↑", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MessageBubble(message: SrishtiMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromSrishti) Arrangement.Start else Arrangement.End
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(if (message.fromSrishti) 0.86f else 0.82f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.fromSrishti) Panel else Color(0xFF1A4550)
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 21.sp
            )
        }
    }
}

package com.srishti.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.srishti.ai.memory.MemoryEngine
import com.srishti.ai.task.TaskManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val memoryEngine = remember { MemoryEngine(applicationContext) }
            val taskManager = remember { TaskManager(applicationContext) }
            val viewModel = remember {
                SrishtiViewModel(memoryEngine = memoryEngine, taskManager = taskManager)
            }
            SrishtiApp(viewModel)
        }
    }
}

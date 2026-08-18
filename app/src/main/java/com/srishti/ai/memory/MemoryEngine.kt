package com.srishti.ai.memory

import android.content.Context
import com.srishti.ai.AppLogger
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class MemoryEngine(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun store(content: String): MemoryItem {
        val memory = MemoryItem(
            id = UUID.randomUUID().toString(),
            content = content,
            timestamp = System.currentTimeMillis()
        )

        try {
            val memories = retrieveMemories().toMutableList()
            memories += memory
            val serialized = JSONArray().apply {
                memories.forEach { item ->
                    put(JSONObject().apply {
                        put(KEY_ID, item.id)
                        put(KEY_CONTENT, item.content)
                        put(KEY_TIMESTAMP, item.timestamp)
                    })
                }
            }
            preferences.edit().putString(KEY_MEMORIES, serialized.toString()).apply()
        } catch (exception: Exception) {
            AppLogger.error("Unable to store memory", exception)
        }
        return memory
    }

    fun retrieveMemories(): List<MemoryItem> {
        val serialized = preferences.getString(KEY_MEMORIES, null) ?: return emptyList()
        return try {
            val memories = JSONArray(serialized)
            buildList {
                for (index in 0 until memories.length()) {
                    val item = memories.optJSONObject(index) ?: continue
                    val id = item.optString(KEY_ID)
                    val content = item.optString(KEY_CONTENT)
                    val timestamp = item.optLong(KEY_TIMESTAMP)
                    if (id.isNotEmpty() && content.isNotEmpty() && timestamp > 0L) {
                        add(MemoryItem(id, content, timestamp))
                    }
                }
            }
        } catch (exception: Exception) {
            AppLogger.error("Unable to read memories", exception)
            emptyList()
        }
    }

    fun clearMemories() {
        try {
            preferences.edit().remove(KEY_MEMORIES).apply()
        } catch (exception: Exception) {
            AppLogger.error("Unable to clear memories", exception)
        }
    }

    private companion object {
        const val PREFERENCES_NAME = "srishti_memory"
        const val KEY_MEMORIES = "memories"
        const val KEY_ID = "id"
        const val KEY_CONTENT = "content"
        const val KEY_TIMESTAMP = "timestamp"
    }
}

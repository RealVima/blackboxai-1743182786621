package com.darknationchat.service

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.java.GenerativeModelFutures
import com.google.ai.client.generativeai.type.content
import com.darknationchat.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService(context: Context) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val generativeModelFutures = GenerativeModelFutures(generativeModel)

    suspend fun getResponse(userMessage: String): Message {
        return try {
            val response = withContext(Dispatchers.IO) {
                generativeModelFutures.generateContent(
                    content {
                        text(userMessage)
                    }
                ).get()
            }
            Message(
                content = response.text ?: "No response from Gemini",
                isFromUser = false
            )
        } catch (e: Exception) {
            Message(
                content = "Error: ${e.message ?: "Unknown error"}",
                isFromUser = false
            )
        }
    }
}
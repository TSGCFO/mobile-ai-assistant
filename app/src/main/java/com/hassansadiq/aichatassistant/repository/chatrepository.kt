package com.HassanSadiq.AIChatAssistant.repository

import android.content.Context
import android.util.Log
import com.HassanSadiq.AIChatAssistant.Constants
import com.HassanSadiq.AIChatAssistant.data.ChatMessage
import com.HassanSadiq.AIChatAssistant.data.PreferencesManager
import com.HassanSadiq.AIChatAssistant.network.ApiService
import com.HassanSadiq.AIChatAssistant.network.CreateUserRequest
import com.HassanSadiq.AIChatAssistant.network.NetworkModule
import com.HassanSadiq.AIChatAssistant.network.UserData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository(private val context: Context) {
    
    private val apiService: ApiService = NetworkModule.apiService
    private val preferencesManager = PreferencesManager(context)
    private val gson = Gson()
    
    suspend fun createAnonymousUser(): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d("ChatRepository", "Creating anonymous user")
            val request = CreateUserRequest(
                appId = Constants.APP_ID,
                tableName = "users",
                data = UserData(provider = "anonymous")
            )
            
            val response = apiService.createUser(request)
            Log.d("ChatRepository", "Create user response: ${response.body()}")
            
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    preferencesManager.saveUserId(userResponse.id)
                    preferencesManager.setLoggedIn(true)
                    Log.d("ChatRepository", "User created successfully: ${userResponse.id}")
                    Result.success(userResponse.id)
                } else {
                    Log.e("ChatRepository", "User response is null")
                    Result.failure(Exception("User response is null"))
                }
            } else {
                Log.e("ChatRepository", "Create user failed: ${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to create user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Exception creating user", e)
            Result.failure(e)
        }
    }
    
    suspend fun sendMessage(message: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            Log.d("ChatRepository", "Sending message: $message")
            val response = apiService.getAIResponse(
                appId = Constants.APP_ID,
                query = message
            )
            Log.d("ChatRepository", "AI response: ${response.body()}")
            
            if (response.isSuccessful) {
                val aiResponse = response.body()
                if (aiResponse != null && !aiResponse.message.content.isNullOrEmpty()) {
                    Log.d("ChatRepository", "AI response received: ${aiResponse.message.content}")
                    Result.success(aiResponse.message.content)
                } else {
                    Log.e("ChatRepository", "AI response is null or empty")
                    Result.failure(Exception("AI response is null or empty"))
                }
            } else {
                Log.e("ChatRepository", "AI request failed: ${response.errorBody()?.string()}")
                Result.failure(Exception("Failed to get AI response: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Exception sending message", e)
            Result.failure(e)
        }
    }
    
    fun saveChatHistory(messages: List<ChatMessage>) {
        try {
            val json = gson.toJson(messages)
            context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString("chat_history", json)
                .apply()
            Log.d("ChatRepository", "Chat history saved: ${messages.size} messages")
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error saving chat history", e)
        }
    }
    
    fun loadChatHistory(): List<ChatMessage> {
        return try {
            val json = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                .getString("chat_history", null)
            if (!json.isNullOrEmpty()) {
                val type = object : TypeToken<List<ChatMessage>>() {}.type
                val messages = gson.fromJson<List<ChatMessage>>(json, type)
                Log.d("ChatRepository", "Chat history loaded: ${messages.size} messages")
                messages
            } else {
                Log.d("ChatRepository", "No chat history found")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error loading chat history", e)
            emptyList()
        }
    }
    
    fun clearChatHistory() {
        try {
            context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("chat_history")
                .apply()
            Log.d("ChatRepository", "Chat history cleared")
        } catch (e: Exception) {
            Log.e("ChatRepository", "Error clearing chat history", e)
        }
    }
}
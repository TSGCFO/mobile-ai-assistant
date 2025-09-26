package com.HassanSadiq.AIChatAssistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.HassanSadiq.AIChatAssistant.data.ChatMessage
import com.HassanSadiq.AIChatAssistant.data.PreferencesManager
import com.HassanSadiq.AIChatAssistant.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    
    private val chatRepository = ChatRepository(application)
    private val preferencesManager = PreferencesManager(application)
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadChatHistory()
    }
    
    private fun loadChatHistory() {
        val history = chatRepository.loadChatHistory()
        val messageLimit = preferencesManager.getMessageLimit()
        _messages.value = if (history.size > messageLimit) {
            history.takeLast(messageLimit)
        } else {
            history
        }
    }
    
    fun sendMessage(content: String) {
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            // Add user message
            val userMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                content = content,
                isFromUser = true
            )
            
            val currentMessages = _messages.value.toMutableList()
            currentMessages.add(userMessage)
            _messages.value = currentMessages
            
            try {
                // Send to AI and get response
                val result = chatRepository.sendMessage(content)
                if (result.isSuccess) {
                    val aiResponse = result.getOrNull()
                    if (!aiResponse.isNullOrEmpty()) {
                        val aiMessage = ChatMessage(
                            id = UUID.randomUUID().toString(),
                            content = aiResponse,
                            isFromUser = false
                        )
                        
                        val updatedMessages = _messages.value.toMutableList()
                        updatedMessages.add(aiMessage)
                        
                        // Apply message limit
                        val messageLimit = preferencesManager.getMessageLimit()
                        val finalMessages = if (updatedMessages.size > messageLimit) {
                            updatedMessages.takeLast(messageLimit)
                        } else {
                            updatedMessages
                        }
                        
                        _messages.value = finalMessages
                        chatRepository.saveChatHistory(finalMessages)
                    }
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Failed to get AI response"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearChat() {
        _messages.value = emptyList()
        chatRepository.clearChatHistory()
    }
    
    fun clearError() {
        _error.value = null
    }
}
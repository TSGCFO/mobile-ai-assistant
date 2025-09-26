package com.HassanSadiq.AIChatAssistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.HassanSadiq.AIChatAssistant.data.PreferencesManager
import com.HassanSadiq.AIChatAssistant.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferencesManager = PreferencesManager(application)
    private val chatRepository = ChatRepository(application)
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun completeOnboarding() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Create anonymous user
                val result = chatRepository.createAnonymousUser()
                if (result.isSuccess) {
                    preferencesManager.setOnboardingCompleted(true)
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Failed to initialize user"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun isOnboardingCompleted(): Boolean {
        return preferencesManager.isOnboardingCompleted()
    }
}
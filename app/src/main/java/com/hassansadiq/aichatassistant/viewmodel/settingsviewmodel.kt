package com.HassanSadiq.AIChatAssistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.HassanSadiq.AIChatAssistant.data.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferencesManager = PreferencesManager(application)
    
    private val _isDarkMode = MutableStateFlow(preferencesManager.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode
    
    private val _messageLimit = MutableStateFlow(preferencesManager.getMessageLimit())
    val messageLimit: StateFlow<Int> = _messageLimit
    
    private val _aiModel = MutableStateFlow(preferencesManager.getAIModel())
    val aiModel: StateFlow<String> = _aiModel
    
    fun toggleTheme() {
        val newMode = !_isDarkMode.value
        _isDarkMode.value = newMode
        preferencesManager.setThemeMode(newMode)
    }
    
    fun updateMessageLimit(limit: Int) {
        _messageLimit.value = limit
        preferencesManager.setMessageLimit(limit)
    }
    
    fun updateAIModel(model: String) {
        _aiModel.value = model
        preferencesManager.setAIModel(model)
    }
}
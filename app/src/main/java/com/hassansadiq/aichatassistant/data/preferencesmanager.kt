package com.HassanSadiq.AIChatAssistant.data

import android.content.Context
import android.content.SharedPreferences
import com.HassanSadiq.AIChatAssistant.Constants

class PreferencesManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
    
    fun saveUserId(userId: String) {
        sharedPreferences.edit()
            .putString(Constants.KEY_USER_ID, userId)
            .apply()
    }
    
    fun getUserId(): String? {
        return sharedPreferences.getString(Constants.KEY_USER_ID, null)
    }
    
    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.KEY_IS_LOGGED_IN, isLoggedIn)
            .apply()
    }
    
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }
    
    fun setThemeMode(isDarkMode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.KEY_THEME_MODE, isDarkMode)
            .apply()
    }
    
    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_THEME_MODE, false)
    }
    
    fun setMessageLimit(limit: Int) {
        sharedPreferences.edit()
            .putInt(Constants.KEY_MESSAGE_LIMIT, limit)
            .apply()
    }
    
    fun getMessageLimit(): Int {
        return sharedPreferences.getInt(Constants.KEY_MESSAGE_LIMIT, Constants.DEFAULT_MESSAGE_LIMIT)
    }
    
    fun setAIModel(model: String) {
        sharedPreferences.edit()
            .putString(Constants.KEY_AI_MODEL, model)
            .apply()
    }
    
    fun getAIModel(): String {
        return sharedPreferences.getString(Constants.KEY_AI_MODEL, Constants.DEFAULT_AI_MODEL) ?: Constants.DEFAULT_AI_MODEL
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.KEY_ONBOARDING_COMPLETED, completed)
            .apply()
    }
    
    fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_ONBOARDING_COMPLETED, false)
    }
    
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
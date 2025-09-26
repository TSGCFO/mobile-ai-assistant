package com.HassanSadiq.AIChatAssistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.HassanSadiq.AIChatAssistant.navigation.AppNavigation
import com.HassanSadiq.AIChatAssistant.ui.theme.AIChatAssistantTheme
import com.HassanSadiq.AIChatAssistant.viewmodel.SettingsViewModel
import android.content.Context
import android.content.SharedPreferences
import com.dexati.analytics.UserApi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var sharedPreferences: SharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        var userId = sharedPreferences.getString("user_id", "")
        UserApi.logUserAnalytics("83288e66-5dd7-4213-95de-f5afceb0417b",userId!!, applicationContext,"https://api.lastapp.ai/")
        // This will fix the double toolbar issue
        actionBar?.hide()
        
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            
            AIChatAssistantTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
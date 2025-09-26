package com.HassanSadiq.AIChatAssistant.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.HassanSadiq.AIChatAssistant.ui.screens.ChatScreen
import com.HassanSadiq.AIChatAssistant.ui.screens.OnboardingScreen
import com.HassanSadiq.AIChatAssistant.ui.screens.SettingsScreen
import com.HassanSadiq.AIChatAssistant.viewmodel.OnboardingViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    val onboardingViewModel: OnboardingViewModel = viewModel()
    
    val startDestination = if (onboardingViewModel.isOnboardingCompleted()) {
        "chat"
    } else {
        "onboarding"
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("chat") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        
        composable("chat") {
            ChatScreen(
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
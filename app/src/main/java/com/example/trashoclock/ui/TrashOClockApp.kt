package com.example.trashoclock.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.trashoclock.ui.main.MainScreen
import com.example.trashoclock.ui.signup.SignUpScreen
import com.example.trashoclock.ui.splash.SplashScreen
import com.example.trashoclock.ui.welcome.WelcomeScreen

enum class Screen { Splash, Welcome, SignUp, Main }

@Composable
fun TrashOClockApp() {
    var backStack by rememberSaveable { mutableStateOf(listOf(Screen.Splash)) }
    val current = backStack.last()

    fun navigate(to: Screen) { backStack = backStack + to }
    fun replace(to: Screen) { backStack = backStack.dropLast(1) + to }
    fun back() { backStack = backStack.dropLast(1) }

    BackHandler(enabled = backStack.size > 1, onBack = ::back)

    when (current) {
        Screen.Splash -> SplashScreen(onFinished = { replace(Screen.Welcome) })
        Screen.Welcome -> WelcomeScreen(onSignUp = { navigate(Screen.SignUp) })
        // Signing up clears the stack, so Back from Home exits instead of returning to the form
        Screen.SignUp -> SignUpScreen(onBack = ::back, onSignedUp = { backStack = listOf(Screen.Main) })
        Screen.Main -> MainScreen()
    }
}

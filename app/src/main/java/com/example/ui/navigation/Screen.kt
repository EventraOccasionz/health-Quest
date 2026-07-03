package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object GetStarted : Screen("get_started")
    object SignIn : Screen("signin")
    object Home : Screen("home")
    object BookAppointment : Screen("book")
    object Reports : Screen("reports")
    object HealthPackages : Screen("packages")
    object Profile : Screen("profile")
    object Support : Screen("support")
    object Notifications : Screen("notifications")
    object Wallet : Screen("wallet")
}

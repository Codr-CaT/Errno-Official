package com.erno.app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object RoleSelection : Screen("role_selection")
    object ShopkeeperLogin : Screen("shopkeeper_login")
    object WorkerLogin : Screen("worker_login")
    object Register : Screen("register")
    object VerifyOTP : Screen("verify_otp/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "verify_otp/$phoneNumber"
    }
    
    // Existing routes
    object Home : Screen("home")
    object Analysis : Screen("analysis")
}

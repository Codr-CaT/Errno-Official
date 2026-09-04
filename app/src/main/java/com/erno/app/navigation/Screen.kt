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
    
    // Shopkeeper Flow Routes
    object ShopkeeperHome : Screen("shopkeeper_home")
    object PostJob : Screen("post_job")
    object PayStructure : Screen("pay_structure")
    object PreviewJob : Screen("preview_job")
    object JobSuccess : Screen("job_success")
    object ShopkeeperMyJobs : Screen("shopkeeper_my_jobs")
    object ShopkeeperApplications : Screen("shopkeeper_applications")
    object ShopkeeperProfile : Screen("shopkeeper_profile")

    // Worker Flow Routes
    object WorkerHome : Screen("worker_home")
    object WorkerJobDetails : Screen("worker_job_details")
    object WorkerAcceptingJob : Screen("worker_accepting_job")
    object WorkerMyJobs : Screen("worker_my_jobs")
    object WorkerEarnings : Screen("worker_earnings")
    object WorkerProfile : Screen("worker_profile")

    // Existing routes
    object Home : Screen("home")
    object Analysis : Screen("analysis")
}

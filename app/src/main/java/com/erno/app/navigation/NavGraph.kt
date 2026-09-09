package com.erno.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erno.app.ui.components.ShopkeeperTab
import com.erno.app.ui.components.WorkerTab
import com.erno.app.ui.screens.auth.RegisterScreen
import com.erno.app.ui.screens.auth.VerifyOTPScreen
import com.erno.app.ui.screens.home.HomeScreen
import com.erno.app.ui.screens.payment.PaymentCheckoutScreen
import com.erno.app.ui.screens.payment.PaymentSuccessScreen
import com.erno.app.ui.screens.role.Role
import com.erno.app.ui.screens.role.RoleSelectionScreen
import com.erno.app.ui.screens.shopkeeper.*
import com.erno.app.ui.screens.splash.SplashScreen
import com.erno.app.ui.screens.worker.*
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
    val shopkeeperViewModel: ShopkeeperViewModel = viewModel()
    val shopkeeperState by shopkeeperViewModel.uiState.collectAsState()

    val workerViewModel: WorkerViewModel = viewModel()
    val workerState by workerViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onGetStartedClick = {
                    navController.navigate(Screen.RoleSelection.route)
                }
            )
        }
        
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onShopkeeperClick = {
                    navController.navigate(Screen.ShopkeeperLogin.route)
                },
                onWorkerClick = {
                    navController.navigate(Screen.WorkerLogin.route)
                },
                onCreateAccount = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.ShopkeeperLogin.route) {
            ShopkeeperLoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSendOTP = { phoneNumber ->
                    navController.navigate("verify_otp/$phoneNumber?role=shopkeeper")
                },
                onCreateAccount = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.WorkerLogin.route) {
            WorkerLoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSendOTP = { phoneNumber ->
                    navController.navigate("verify_otp/$phoneNumber?role=worker")
                },
                onCreateAccount = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterClick = { phoneNumber, role ->
                    val roleParam = if (role == Role.WORKER) "worker" else "shopkeeper"
                    navController.navigate("verify_otp/$phoneNumber?role=$roleParam")
                },
                onLoginClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "verify_otp/{phoneNumber}?role={role}",
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("role") {
                    type = NavType.StringType
                    defaultValue = "shopkeeper"
                }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val role = backStackEntry.arguments?.getString("role") ?: "shopkeeper"

            VerifyOTPScreen(
                phoneNumber = phoneNumber,
                onBackClick = {
                    navController.popBackStack()
                },
                onVerifyClick = { otp ->
                    val targetRoute = if (role == "worker") Screen.WorkerHome.route else Screen.ShopkeeperHome.route
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.RoleSelection.route) { inclusive = true }
                    }
                },
                onEditClick = {
                    navController.popBackStack()
                },
                onResendClick = {
                    // Resend logic
                }
            )
        }

        // Shopkeeper Flow Routes
        composable(Screen.ShopkeeperHome.route) {
            ShopkeeperDashboardScreen(
                state = shopkeeperState,
                onPostNewJobClick = {
                    shopkeeperViewModel.resetDraft()
                    navController.navigate(Screen.PostJob.route)
                },
                onViewAllClick = {
                    navController.navigate(Screen.ShopkeeperMyJobs.route)
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        ShopkeeperTab.HOME -> { /* Already here */ }
                        ShopkeeperTab.JOBS -> navController.navigate(Screen.ShopkeeperMyJobs.route)
                        ShopkeeperTab.APPLICATIONS -> navController.navigate(Screen.ShopkeeperApplications.route)
                        ShopkeeperTab.PROFILE -> navController.navigate(Screen.ShopkeeperProfile.route)
                    }
                }
            )
        }

        composable(Screen.PostJob.route) {
            PostJobScreen(
                state = shopkeeperState,
                onBackClick = {
                    navController.popBackStack()
                },
                onTitleChange = shopkeeperViewModel::updateTitle,
                onCategoryChange = shopkeeperViewModel::updateCategory,
                onLocationChange = shopkeeperViewModel::updateLocation,
                onDescriptionChange = shopkeeperViewModel::updateDescription,
                onRemoveDuration = shopkeeperViewModel::removeDurationPay,
                onAddMoreDurationClick = {
                    navController.navigate(Screen.PayStructure.route)
                },
                onPreviewJobClick = {
                    navController.navigate(Screen.PreviewJob.route)
                }
            )
        }

        composable(Screen.PayStructure.route) {
            PayStructureScreen(
                state = shopkeeperState,
                onBackClick = {
                    navController.popBackStack()
                },
                onUpdatePrice = shopkeeperViewModel::updatePrice,
                onAddMoreDuration = shopkeeperViewModel::addDurationPay,
                onDoneClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.PreviewJob.route) {
            PreviewJobScreen(
                state = shopkeeperState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.popBackStack()
                },
                onPostJobClick = {
                    val postedJob = shopkeeperViewModel.postJob()
                    workerViewModel.addJobFromShopkeeper(postedJob)
                    navController.navigate("payment_checkout/250?jobTitle=${postedJob.title}")
                }
            )
        }

        composable(Screen.JobSuccess.route) {
            JobSuccessScreen(
                state = shopkeeperState,
                onGoToMyJobsClick = {
                    navController.navigate(Screen.ShopkeeperMyJobs.route) {
                        popUpTo(Screen.ShopkeeperHome.route)
                    }
                },
                onShareJobClick = {
                    // Share job
                }
            )
        }

        composable(Screen.ShopkeeperMyJobs.route) {
            ShopkeeperMyJobsScreen(
                state = shopkeeperState,
                onBackClick = {
                    navController.popBackStack()
                },
                onPayWorkerClick = { job ->
                    val amount = job.payStructure.maxOfOrNull { it.price } ?: 349
                    navController.navigate("payment_checkout/$amount?jobTitle=${job.title}")
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        ShopkeeperTab.HOME -> navController.navigate(Screen.ShopkeeperHome.route)
                        ShopkeeperTab.JOBS -> { /* Already here */ }
                        ShopkeeperTab.APPLICATIONS -> navController.navigate(Screen.ShopkeeperApplications.route)
                        ShopkeeperTab.PROFILE -> navController.navigate(Screen.ShopkeeperProfile.route)
                    }
                }
            )
        }

        composable(Screen.ShopkeeperApplications.route) {
            ShopkeeperApplicationsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        ShopkeeperTab.HOME -> navController.navigate(Screen.ShopkeeperHome.route)
                        ShopkeeperTab.JOBS -> navController.navigate(Screen.ShopkeeperMyJobs.route)
                        ShopkeeperTab.APPLICATIONS -> { /* Already here */ }
                        ShopkeeperTab.PROFILE -> navController.navigate(Screen.ShopkeeperProfile.route)
                    }
                }
            )
        }

        composable(Screen.ShopkeeperProfile.route) {
            ShopkeeperProfileScreen(
                state = shopkeeperState,
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(0)
                    }
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        ShopkeeperTab.HOME -> navController.navigate(Screen.ShopkeeperHome.route)
                        ShopkeeperTab.JOBS -> navController.navigate(Screen.ShopkeeperMyJobs.route)
                        ShopkeeperTab.APPLICATIONS -> navController.navigate(Screen.ShopkeeperApplications.route)
                        ShopkeeperTab.PROFILE -> { /* Already here */ }
                    }
                }
            )
        }

        // Worker Flow Routes
        composable(Screen.WorkerHome.route) {
            WorkerHomeScreen(
                state = workerState,
                onJobSelect = { job ->
                    workerViewModel.selectJob(job)
                    navController.navigate(Screen.WorkerJobDetails.route)
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        WorkerTab.HOME -> { /* Already here */ }
                        WorkerTab.MY_JOBS -> navController.navigate(Screen.WorkerMyJobs.route)
                        WorkerTab.EARNINGS -> navController.navigate(Screen.WorkerEarnings.route)
                        WorkerTab.PROFILE -> navController.navigate(Screen.WorkerProfile.route)
                    }
                }
            )
        }

        composable(Screen.WorkerJobDetails.route) {
            WorkerJobDetailsScreen(
                state = workerState,
                onBackClick = {
                    navController.popBackStack()
                },
                onDurationSelect = workerViewModel::selectDurationPay,
                onAcceptJobClick = {
                    workerViewModel.acceptCurrentJob()
                    navController.navigate(Screen.WorkerAcceptingJob.route)
                }
            )
        }

        composable(Screen.WorkerAcceptingJob.route) {
            WorkerAcceptingJobScreen(
                state = workerState,
                onViewJobInfoClick = {
                    navController.navigate(Screen.WorkerMyJobs.route) {
                        popUpTo(Screen.WorkerHome.route)
                    }
                }
            )
        }

        composable(Screen.WorkerMyJobs.route) {
            WorkerMyJobsScreen(
                state = workerState,
                onBackClick = {
                    navController.popBackStack()
                },
                onJobClick = { job ->
                    workerViewModel.selectJob(job)
                    navController.navigate(Screen.WorkerJobDetails.route)
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        WorkerTab.HOME -> navController.navigate(Screen.WorkerHome.route)
                        WorkerTab.MY_JOBS -> { /* Already here */ }
                        WorkerTab.EARNINGS -> navController.navigate(Screen.WorkerEarnings.route)
                        WorkerTab.PROFILE -> navController.navigate(Screen.WorkerProfile.route)
                    }
                }
            )
        }

        composable(Screen.WorkerEarnings.route) {
            WorkerEarningsScreen(
                state = workerState,
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        WorkerTab.HOME -> navController.navigate(Screen.WorkerHome.route)
                        WorkerTab.MY_JOBS -> navController.navigate(Screen.WorkerMyJobs.route)
                        WorkerTab.EARNINGS -> { /* Already here */ }
                        WorkerTab.PROFILE -> navController.navigate(Screen.WorkerProfile.route)
                    }
                }
            )
        }

        composable(Screen.WorkerProfile.route) {
            WorkerProfileScreen(
                state = workerState,
                onBackClick = {
                    navController.popBackStack()
                },
                onLogoutClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(0)
                    }
                },
                onNavigateTab = { tab ->
                    when (tab) {
                        WorkerTab.HOME -> navController.navigate(Screen.WorkerHome.route)
                        WorkerTab.MY_JOBS -> navController.navigate(Screen.WorkerMyJobs.route)
                        WorkerTab.EARNINGS -> navController.navigate(Screen.WorkerEarnings.route)
                        WorkerTab.PROFILE -> { /* Already here */ }
                    }
                }
            )
        }

        // Payment Gateway Routes
        composable(
            route = "payment_checkout/{amount}?jobTitle={jobTitle}",
            arguments = listOf(
                navArgument("amount") { type = NavType.IntType; defaultValue = 250 },
                navArgument("jobTitle") { type = NavType.StringType; defaultValue = "Shift Payment" }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getInt("amount") ?: 250
            val jobTitle = backStackEntry.arguments?.getString("jobTitle") ?: "Shift Payment"

            PaymentCheckoutScreen(
                amount = amount,
                jobTitle = jobTitle,
                onBackClick = {
                    navController.popBackStack()
                },
                onPaymentSuccess = { txnId ->
                    navController.navigate("payment_success/$txnId?amount=${amount + 18}") {
                        popUpTo(Screen.RoleSelection.route)
                    }
                }
            )
        }

        composable(
            route = "payment_success/{txnId}?amount={amount}",
            arguments = listOf(
                navArgument("txnId") { type = NavType.StringType; defaultValue = "TXN_88492018" },
                navArgument("amount") { type = NavType.IntType; defaultValue = 268 }
            )
        ) { backStackEntry ->
            val txnId = backStackEntry.arguments?.getString("txnId") ?: "TXN_88492018"
            val amount = backStackEntry.arguments?.getInt("amount") ?: 268

            PaymentSuccessScreen(
                transactionId = txnId,
                amount = amount,
                onDoneClick = {
                    navController.navigate(Screen.JobSuccess.route) {
                        popUpTo(Screen.ShopkeeperHome.route)
                    }
                }
            )
        }

        // Existing routes
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}

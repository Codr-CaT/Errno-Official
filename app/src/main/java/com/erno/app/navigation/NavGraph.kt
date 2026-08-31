package com.erno.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.erno.app.ui.screens.home.HomeScreen
import com.erno.app.ui.screens.role.RoleSelectionScreen
import com.erno.app.ui.screens.shopkeeper.ShopkeeperLoginScreen
import com.erno.app.ui.screens.splash.SplashScreen
import com.erno.app.ui.screens.worker.WorkerLoginScreen
import com.erno.app.ui.screens.auth.VerifyOTPScreen
import com.erno.app.ui.screens.auth.RegisterScreen
import com.erno.app.ui.screens.role.Role
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
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
                    navController.navigate(Screen.VerifyOTP.createRoute(phoneNumber))
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
                    navController.navigate(Screen.VerifyOTP.createRoute(phoneNumber))
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
                    navController.navigate(Screen.VerifyOTP.createRoute(phoneNumber))
                },
                onLoginClick = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.VerifyOTP.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            VerifyOTPScreen(
                phoneNumber = phoneNumber,
                onBackClick = {
                    navController.popBackStack()
                },
                onVerifyClick = { otp ->
                    navController.navigate(Screen.Home.route)
                },
                onEditClick = {
                    navController.popBackStack()
                },
                onResendClick = {
                    // TODO
                }
            )
        }

        // Existing routes
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}

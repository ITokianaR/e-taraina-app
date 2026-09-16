package com.example.e_taraina.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.e_taraina.domain.models.UserRole
import com.example.e_taraina.ui.features.home.HomeScreen
import com.example.e_taraina.ui.features.homeadmin.HomeAdminScreen
import com.example.e_taraina.ui.features.login.LoginScreen
import com.example.e_taraina.ui.features.reportlist.ReportListScreen

@Composable
fun ETarainaNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { username, role ->
                    val destination = if (role == UserRole.ADMIN) {
                        Screen.HomeAdmin.route
                    } else {
                        Screen.Home.createRoute(username)
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Home.route,
            arguments = listOf(navArgument(Screen.Home.ARG_USERNAME) { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString(Screen.Home.ARG_USERNAME).orEmpty()
            HomeScreen(
                username = username,
                onComplaintSubmitted = {
                    navController.navigate(Screen.ReportList.route)
                },
                onViewComplaintsClick = {
                    navController.navigate(Screen.ReportList.route)
                }
            )
        }

        composable(Screen.ReportList.route) {
            ReportListScreen()
        }

        composable(Screen.HomeAdmin.route) {
            HomeAdminScreen()
        }
    }
}

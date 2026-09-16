package com.example.e_taraina.ui.nav

sealed class Screen(val route: String) {
    data object Login : Screen("login")

    data object Home : Screen("home/{username}") {
        const val ARG_USERNAME = "username"
        fun createRoute(username: String) = "home/$username"
    }

    // Reserved for when the remaining wireframe screens are wired up:
    // data object HomeAdmin : Screen("home_admin")
    // data object FillReport : Screen("fill_report")
    // data object ReportList : Screen("report_list")
    // data object ReportDetails : Screen("report_details/{id}")
}

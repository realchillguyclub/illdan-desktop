package com.illdan.desktop.presentation.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.illdan.desktop.core.navigation.NavGraphContributor
import com.illdan.desktop.core.navigation.NavRoutes
import com.illdan.desktop.presentation.login.LoginScreen
import org.koin.compose.viewmodel.koinViewModel

object AuthNavGraph: NavGraphContributor {
    override val graphRoute: NavRoutes
        get() = NavRoutes.LoginGraph
    override val startDestination: String
        get() = NavRoutes.LoginScreen.route
    override val priority: Int
        get() = 0

    override fun NavGraphBuilder.registerGraph(navController: NavHostController) {
        navigation(
            route = graphRoute.route,
            startDestination = startDestination
        ) {
            composable(NavRoutes.LoginScreen.route) {
                LoginScreen(
                    viewModel = koinViewModel()
                )
            }
        }
    }
}
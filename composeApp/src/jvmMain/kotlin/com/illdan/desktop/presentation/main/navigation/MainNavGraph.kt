package com.illdan.desktop.presentation.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.illdan.desktop.core.navigation.NavGraphContributor
import com.illdan.desktop.core.navigation.NavRoutes
import com.illdan.desktop.presentation.main.MainScreen

object MainNavGraph: NavGraphContributor {
    override val graphRoute: NavRoutes
        get() = NavRoutes.MainGraph
    override val startDestination: String
        get() = NavRoutes.MainScreen.route
    override val priority: Int
        get() = 10

    override fun NavGraphBuilder.registerGraph(navController: NavHostController) {
        navigation(
            route = graphRoute.route,
            startDestination = startDestination
        ) {
            composable(NavRoutes.MainScreen.route) {
                MainScreen()
            }
        }
    }
}
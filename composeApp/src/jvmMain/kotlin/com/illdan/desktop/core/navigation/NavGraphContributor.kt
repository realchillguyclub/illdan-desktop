package com.illdan.desktop.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavGraphContributor {
    val graphRoute: NavRoutes
    val startDestination: String
    val priority: Int get() = 100

    fun NavGraphBuilder.registerGraph(navController: NavHostController)
}
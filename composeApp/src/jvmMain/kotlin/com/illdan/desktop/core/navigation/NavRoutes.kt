package com.illdan.desktop.core.navigation

sealed class NavRoutes(val route: String) {

    // Main
    data object MainGraph: NavRoutes("main/graph")
    data object MainScreen: NavRoutes("main/screen")

    // Splash
    data object SplashGraph: NavRoutes("splash/graph")
    data object SplashScreen: NavRoutes("splash/screen")

    // Login
    data object LoginGraph: NavRoutes("login/graph")
    data object LoginScreen: NavRoutes("login/screen")
}
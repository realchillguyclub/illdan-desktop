package com.illdan.desktop.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.illdan.desktop.core.util.GlobalEventManager
import org.koin.compose.getKoin

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val koin = getKoin()
    val contributors = remember { // remember로 재구성 시 새로운 객체 생성 방지
        koin.getAll<NavGraphContributor>().sortedBy { it.priority }
    }
    val start = contributors
        .firstOrNull { it.graphRoute == NavRoutes.MainGraph }
        ?: error("해당 Graph를 찾을 수 없습니다.")
    val duration = 300
    val easing = tween<IntOffset>(durationMillis = duration)

    LaunchedEffect(Unit) {
        GlobalEventManager.navigateToLoginScreen.collect {
            navController.navigate(NavRoutes.LoginGraph.route)
        }
    }

    NavHost(
        navController = navController,
        startDestination = start.graphRoute.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { full -> full },
                animationSpec = easing
            ) + fadeIn(animationSpec = tween(duration))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { full -> -full / 4 },
                animationSpec = easing
            ) + fadeOut(animationSpec = tween((duration * 0.8).toInt()))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { full -> -full / 4 },
                animationSpec = easing
            ) + fadeIn(animationSpec = tween((duration * 0.8).toInt()))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { full -> full },
                animationSpec = easing
            ) + fadeOut(animationSpec = tween(duration))
        }
    ) {
        contributors.forEach { with(it) { registerGraph(navController) } }
    }
}
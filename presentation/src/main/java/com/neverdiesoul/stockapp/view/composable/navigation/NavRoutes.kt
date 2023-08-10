package com.neverdiesoul.stockapp.view.composable.navigation

sealed class NavRoutes(val route: String) {
    object Intro : NavRoutes("intro")
    object Main : NavRoutes("main")
    object Detail : NavRoutes("detail")
}

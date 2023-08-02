package com.neverdiesoul.stockapp.view.composable.navigation

import com.neverdiesoul.stockapp.R

sealed class BottomNavBarRoutes(val route: String) {
    object NativeScreen : NavRoutes("native")
    object WebviewScreen : NavRoutes("webview")
}

object BottomNavigationBar {
    val BarItems = listOf(
        BarItem("native" , R.drawable.baseline_android_24, BottomNavBarRoutes.NativeScreen.route),
        BarItem("webview", R.drawable.baseline_web_24, BottomNavBarRoutes.WebviewScreen.route)
    )
}

data class BarItem(val title: String, val imgResId: Int, val route: String)

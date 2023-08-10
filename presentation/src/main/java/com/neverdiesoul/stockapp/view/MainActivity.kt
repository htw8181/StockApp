package com.neverdiesoul.stockapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.view.composable.navigation.Detail
import com.neverdiesoul.stockapp.view.composable.navigation.Intro
import com.neverdiesoul.stockapp.view.composable.navigation.Main
import com.neverdiesoul.stockapp.view.composable.navigation.NavRoutes
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.IntroViewModel
import com.neverdiesoul.stockapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
private fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.Intro.route) {
        composable(NavRoutes.Intro.route) {
            Intro(navController, hiltViewModel<IntroViewModel>())
        }

        composable(NavRoutes.Main.route) {
            Main(navController, hiltViewModel<MainViewModel>())
        }

        composable(NavRoutes.Detail.route+"/{marketCode}/{marketName}",
            arguments = listOf(navArgument("marketCode",{ type = NavType.StringType}),
                navArgument("marketName",{ type = NavType.StringType})))
        { backStackEntry ->
            val marketCode = backStackEntry.arguments?.getString("marketCode")
            val marketName = backStackEntry.arguments?.getString("marketName")
            Detail(navController, hiltViewModel<DetailViewModel>(), marketCode, marketName)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainActivityPreview() {
    StockAppTheme {
        MainScreen()
    }
}
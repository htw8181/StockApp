package com.neverdiesoul.stockapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neverdiesoul.domain.model.CoinMarketCode
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

        composable(NavRoutes.Detail.route) {
            val coinMarketCode = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<CoinMarketCode>("coinMarketCode")
            }?.let {
                Detail(navController, hiltViewModel<DetailViewModel>(), it)
            }
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
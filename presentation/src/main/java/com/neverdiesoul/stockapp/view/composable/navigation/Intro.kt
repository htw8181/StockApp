package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.neverdiesoul.stockapp.viewmodel.IntroViewModel

@Composable
fun Intro(navController: NavHostController, viewModel: IntroViewModel = hiltViewModel()) {
    val stateToGoMain: Boolean by viewModel.stateToGoMain.observeAsState(initial = false)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Intro Screen")
        Button(onClick = {
            navController.navigate(NavRoutes.Main.route) {
                launchSingleTop = true
                popUpTo(NavRoutes.Intro.route) {
                    inclusive = true
                }
            }
        }) {
            Text("Go Main")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getCoinMarketCodeAllFromRemote()
    }

    LaunchedEffect(stateToGoMain) {
        Log.d("1111","11111")
        navController.navigate(NavRoutes.Main.route) {
            launchSingleTop = true
            popUpTo(NavRoutes.Intro.route) {
                inclusive = true
            }
        }
    }
}
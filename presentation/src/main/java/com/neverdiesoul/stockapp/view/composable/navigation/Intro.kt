package com.neverdiesoul.stockapp.view.composable.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.neverdiesoul.stockapp.viewmodel.IntroViewModel

@Composable
fun Intro(navController: NavHostController, viewModel: IntroViewModel = hiltViewModel()) {
    viewModel.getCoinMarketCodeAll()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Intro Screen")
    }
}
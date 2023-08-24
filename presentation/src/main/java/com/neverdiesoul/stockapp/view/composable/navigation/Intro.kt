package com.neverdiesoul.stockapp.view.composable.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.viewmodel.IntroViewModel

@Composable
fun Intro(navController: NavHostController, viewModel: IntroViewModel = hiltViewModel()) {
    val stateToGoMain: Boolean by viewModel.stateToGoMain.observeAsState(initial = false)
    val bitcoinLottie by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.bitcoin_lottie))
    
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieAnimation(composition = bitcoinLottie, iterations = LottieConstants.IterateForever, contentScale = ContentScale.Fit)
        Text(text = "업비트 따라해보기", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, textAlign = TextAlign.Center))
    }

    LaunchedEffect(Unit) {
        viewModel.getCoinMarketCodeAllFromRemote()
    }

    LaunchedEffect(stateToGoMain) {
        if (stateToGoMain) {
            navController.navigate(NavRoutes.Main.route) {
                launchSingleTop = true
                popUpTo(NavRoutes.Intro.route) {
                    inclusive = true
                }
            }
        }
    }
}
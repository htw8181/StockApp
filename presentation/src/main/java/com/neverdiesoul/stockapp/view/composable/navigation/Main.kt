package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.neverdiesoul.stockapp.viewmodel.MainViewModel

@Composable
fun Main(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Main Screen")
    }
    Log.d("1111","22222")
    LaunchedEffect(Unit) {
        viewModel.getRealTimeStock()
    }
}
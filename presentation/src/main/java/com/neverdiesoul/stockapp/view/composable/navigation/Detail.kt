package com.neverdiesoul.stockapp.view.composable.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController: NavHostController, viewModel: DetailViewModel, marketCode: String?, marketName: String?) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                "$marketName(${viewModel.getMarketCodeToDisplay(marketCode)})",
                modifier = Modifier.padding(start = 10.dp)) },
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Settings",
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            )
        },
        bottomBar = {}
    ) { paddingValues ->

    }
}

private fun getMarketCodeToDisplay(marketCode: String?): String {
    return marketCode?.let {
        val seperatorSymbol = "/"
        val results = it.replace("-",seperatorSymbol).split(seperatorSymbol)
        "${results[1]}$seperatorSymbol${results[0]}"
    } ?: ""
}
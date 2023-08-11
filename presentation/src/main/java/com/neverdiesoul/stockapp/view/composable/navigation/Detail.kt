package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.viewmodel.BaseRealTimeViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.NONE_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.TabGroup

private const val TAG = "NavDetailView"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController: NavHostController, viewModel: DetailViewModel?, coinMarketCode: CoinMarketCode) {
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableStateOf(NONE_STATE) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                "${coinMarketCode.korean_name}(${viewModel?.getMarketCodeToDisplay(coinMarketCode.market)})",
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ))
        {
            Row {
                TabRow(selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    containerColor = Color(red = 9, green = 54, blue = 135), //탭 백그라운드 컬러
                    tabs = {
                        enumValues<TabGroup>().forEachIndexed { tabIndex, tabValue ->
                            Tab(modifier = Modifier,
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color(red = 157, green = 175, blue = 207),
                                selected = tabIndex == selectedTabIndex,
                                onClick = {
                                    selectedTabIndex = tabIndex
                                },
                                content = { Text(text = stringResource(id = tabValue.resId), modifier = Modifier.padding(10.dp)) })
                        }
                    },
                    indicator = {}
                )
            }
            
            Row(modifier = Modifier.weight(1f,true)) {
                Text("content body1",modifier = Modifier.weight(0.4f,true))
                /*LazyColumn(modifier = Modifier.weight(0.4f,true)) {

                }*/
                OrderTabContent(modifier = Modifier.weight(0.6f, true).fillMaxHeight()) {
                    TestComponent()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel?.setViewEvent(object : BaseRealTimeViewModel.ViewEvent {
            override fun viewOnReady() {
                viewModel.requestRealTimeCoinData(coinMarketCode)
            }

            override fun viewOnExit() {
                Toast.makeText(context,"viewOnExit", Toast.LENGTH_SHORT).show()
                Log.d(TAG,"viewOnExit")
            }
        })
        viewModel?.getRealTimeStock()
    }
}

@Composable
private fun OrderTabContent(modifier: Modifier, content: @Composable ()->Unit) {
    Box(modifier = modifier) {
        content()
    }
}

@Composable
private fun TestComponent() {
    Text("content body2",modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
private fun DetailPreview() {
    StockAppTheme {
        Surface {
            Detail(rememberNavController(), null, CoinMarketCode(market = "KRW-BTC", korean_name = "비트코인", english_name = "BitCoin"))
        }
    }
}
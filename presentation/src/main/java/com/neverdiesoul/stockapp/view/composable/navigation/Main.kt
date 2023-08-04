package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.viewmodel.BTC_STATE
import com.neverdiesoul.stockapp.viewmodel.CoinGroup
import com.neverdiesoul.stockapp.viewmodel.KRW_STATE
import com.neverdiesoul.stockapp.viewmodel.MainViewModel
import com.neverdiesoul.stockapp.viewmodel.NONE_STATE
import com.neverdiesoul.stockapp.viewmodel.USDT_STATE

private const val TAG = "NavMainView"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(navController: NavHostController, viewModel: MainViewModel?) {
    var selectedTabIndex by remember { mutableStateOf(NONE_STATE) }

    val coinMarketCodes: List<CoinMarketCode> by viewModel?.coinMarketCodes!!.observeAsState(initial = mutableListOf())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name),
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
        bottomBar = {
            BottomNavigationBar(rememberNavController())
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ))
        {
            /**
             * TODO
             * 1. 실시간 코인 데이터를 LazyColumn 으로 구성
             */
            Spacer(modifier = Modifier
                .background(color = Color(red = 9, green = 54, blue = 135))
                .height(3.dp)
                .fillMaxWidth())

            Row(modifier = Modifier.padding(10.dp)) {
                val selectedColor = Color(red = 9, green = 54, blue = 135)
                TabRow(selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .border(width = 1.dp, color = Color.Gray, shape = RectangleShape),
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    tabs = {
                        enumValues<CoinGroup>().forEachIndexed { marketCodeIndex, marketCodeName ->
                            Tab(modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = if (marketCodeIndex == selectedTabIndex) selectedColor else Color.Transparent,
                                    shape = RectangleShape
                                )
                                .drawBehind {
                                    drawLine(
                                        Color.Gray,
                                        Offset(size.width, 0f),
                                        Offset(size.width, size.height),
                                        1.dp.toPx()
                                    )
                                },
                                selectedContentColor = selectedColor,
                                unselectedContentColor = Color.Gray,
                                selected = marketCodeIndex == selectedTabIndex,
                                onClick = { selectedTabIndex = marketCodeIndex },
                                content = { Text(text = marketCodeName.name, modifier = Modifier.padding(10.dp)) })
                        }
                    },
                    indicator = {}
                )
            }
        }
    }
    Log.d("1111","22222")
    LaunchedEffect(Unit) {
        viewModel?.getCoinMarketCodeAllFromLocal()
        viewModel?.getRealTimeStock()
    }

    LaunchedEffect(coinMarketCodes) {
        coinMarketCodes.let {
            val marketCodesGroup = it.onEach {
                //Log.d(TAG,it.toString())
            }.groupBy { coinMarketCode ->
                when {
                    coinMarketCode.market.startsWith("KRW-") -> CoinGroup.KRW.name
                    coinMarketCode.market.startsWith("BTC-") -> CoinGroup.BTC.name
                    coinMarketCode.market.startsWith("USDT-") -> CoinGroup.USDT.name
                    else -> "ETC"
                }
            }.onEach { entry ->
                Log.d(TAG,"marketCodesGroupName is ${entry.key}")
                entry.value.forEach { coinMarketCode ->
                    Log.d(TAG,"${entry.key}'s member is ${coinMarketCode.market}")
                }

                with(entry) {
                    when(key) {
                        CoinGroup.KRW.name -> viewModel?.setKrwGroupMarketCodes(value)
                        CoinGroup.BTC.name -> viewModel?.setBtcGroupMarketCodes(value)
                        CoinGroup.USDT.name -> viewModel?.setUsdtGroupMarketCodes(value)
                    }
                }
            }
            Log.d(TAG,"marketCodesGroup size ${marketCodesGroup.size}")
        }
    }

    LaunchedEffect(selectedTabIndex) {
        Log.d(TAG,"current selectedTabIndex $selectedTabIndex")
        if (selectedTabIndex >= 0) {
            // TODO KRW/BTC/USDT 탭을 클릭할 때마다 해당 마켓으로 웹소켓 통신 하도록 로직 적용
            when(selectedTabIndex) {
                KRW_STATE -> {
                    //viewModel?.getRealTimeStock()
                }
                BTC_STATE -> {
                    //viewModel?.getRealTimeStock()
                }
                USDT_STATE -> {
                    //viewModel?.getRealTimeStock()
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BottomNavigationBar.BarItems.forEach { barItem ->
            NavigationBarItem(selected = currentRoute == barItem.route,
                onClick = {
                    //TODO : 네이티브 와 웹뷰 화면 이동
                },
                icon = { Icon(painter = painterResource(id = barItem.imgResId), contentDescription = barItem.title)},
                label = { Text(text = barItem.title)}
            )
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    StockAppTheme {
        Surface {
            Main(rememberNavController(),null)
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(rememberNavController())
}
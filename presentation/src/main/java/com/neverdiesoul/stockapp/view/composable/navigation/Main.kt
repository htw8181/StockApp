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
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllFromLocalUseCase
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(navController: NavHostController, viewModel: MainViewModel?) {

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
                var selectedTabIndex by remember { mutableStateOf(0) }

                val tabTitles = listOf("KRW","BTC","USDT")
                val selectedColor = Color(red = 9, green = 54, blue = 135)
                TabRow(selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .border(width = 1.dp, color = Color.Gray, shape = RectangleShape),
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    tabs = {
                        tabTitles.forEachIndexed { tabIndex, tabTitle ->
                            Tab(modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = if (tabIndex == selectedTabIndex) selectedColor else Color.Transparent,
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
                                selected = tabIndex == selectedTabIndex,
                                onClick = { selectedTabIndex = tabIndex },
                                content = { Text(text = tabTitle, modifier = Modifier.padding(10.dp)) })
                        }
                    },
                    indicator = {}
                )
            }

            val coinMarketCodes: List<CoinMarketCode> by viewModel?.coinMarketCodes!!.observeAsState(initial = mutableListOf())
            coinMarketCodes.forEach {
                Log.d(GetCoinMarketCodeAllFromLocalUseCase::class.simpleName,it.toString())
            }
        }
    }
    Log.d("1111","22222")
    LaunchedEffect(Unit) {
        viewModel?.getCoinMarketCodeAllFromLocal()
        viewModel?.getRealTimeStock()
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
package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ))
        {
            /**
             * TODO
             * 1. KRW,BTC,USDT 탭버튼 구성
             * 2. 실시간 코인 데이터를 LazyColumn 으로 구성
             */
        }
    }
    Log.d("1111","22222")
    LaunchedEffect(Unit) {
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
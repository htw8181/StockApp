package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.viewmodel.BaseRealTimeViewModel
import com.neverdiesoul.stockapp.viewmodel.MainViewModel
import com.neverdiesoul.stockapp.viewmodel.MainViewModel.CoinGroup
import com.neverdiesoul.stockapp.viewmodel.MainViewModel.Companion.KRW_STATE
import com.neverdiesoul.stockapp.viewmodel.MainViewModel.Companion.NONE_STATE
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

private const val TAG = "NavMainView"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(navController: NavHostController, viewModel: MainViewModel) {
    val funcName = object{}.javaClass.enclosingMethod?.name
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableStateOf(NONE_STATE) }

    val coinMarketCodes: List<CoinMarketCode> by viewModel.coinMarketCodes.observeAsState(initial = mutableListOf())

    val coinCurrentPrices: List<CoinCurrentPrice> by viewModel.coinCurrentPrices.observeAsState(initial = mutableListOf())

    //val realTimeCoinCurrentPrice by viewModel.sharedFlow.collectAsState(initial = null)
    var realTimeCoinCurrentPrice: CoinCurrentPriceForView? by remember {
        mutableStateOf(null)
    }

    val coinCurrentPriceForViewList = remember {
        mutableStateListOf<CoinCurrentPriceForView>()
    }

    val onTabClick: (Int)->Unit = {
        selectedTabIndex = it
        // KRW/BTC/USDT 탭을 클릭할 때마다 해당 마켓 코드로 현재가 조회후 실시간 코인 정보 요청
        viewModel.getCoinCurrentPrice(selectedTabIndex)
    }

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
            Spacer(modifier = Modifier
                .background(color = Color(red = 9, green = 54, blue = 135))
                .height(3.dp)
                .fillMaxWidth())

            Row(modifier = Modifier.padding(10.dp)) {
                val selectedColor = Color(red = 9, green = 54, blue = 135)
                TabRow(selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(0.5f).border(width = 1.dp, color = Color.Gray, shape = RectangleShape),
                    containerColor = Color.White,
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
                                onClick = {
                                    onTabClick(marketCodeIndex)
                                },
                                content = { Text(text = marketCodeName.name, modifier = Modifier.padding(10.dp)) })
                        }
                    },
                    indicator = {}
                )
            }

            Spacer(modifier = Modifier
                .background(color = Color(red = 241, green = 241, blue = 244))
                .height(1.dp)
                .fillMaxWidth())
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .height(35.dp)
                .background(color = Color(red = 244, green = 245, blue = 248))) {
                Text(text = stringResource(R.string.main_list_header1), modifier = Modifier.weight(.25f, true), textAlign = TextAlign.Center)
                Text(text = stringResource(R.string.main_list_header2), modifier = Modifier.weight(.25f, true), textAlign = TextAlign.Center)
                Text(text = stringResource(R.string.main_list_header3), modifier = Modifier.weight(.25f, true), textAlign = TextAlign.Center)
                Text(text = stringResource(R.string.main_list_header4), modifier = Modifier.weight(.25f, true), textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier
                .background(color = Color(red = 241, green = 241, blue = 244))
                .height(1.dp)
                .fillMaxWidth())

            LazyColumn {
                val onListItemClick = { coinMarketCode: CoinMarketCode ->
                    Toast.makeText(context, coinMarketCode.market, Toast.LENGTH_SHORT).show()
                    // TODO 웹소켓 닫고 가야 한다.
                    viewModel.closeRealTimeStock()
                    navController.currentBackStackEntry?.savedStateHandle?.set(key = "coinMarketCode", value = coinMarketCode)
                    navController.navigate(NavRoutes.Detail.route) {
                        launchSingleTop = true
                    }
                }

                items(coinCurrentPriceForViewList) { coinCurrentPriceForView ->
                    CurrentPriceItem(coinCurrentPriceForView, viewModel, onListItemClick)
                    Spacer(modifier = Modifier
                        .background(color = Color(red = 241, green = 241, blue = 244))
                        .height(1.dp)
                        .fillMaxWidth())
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setViewEvent(object : BaseRealTimeViewModel.ViewEvent {
            override fun viewOnReady() {
                onTabClick(KRW_STATE)
            }

            override fun viewOnExit() {
                Toast.makeText(context,"viewOnExit", Toast.LENGTH_SHORT).show()
                Log.d(TAG,"viewOnExit")
            }
        })
        viewModel.getCoinMarketCodeAllFromLocal()
    }

    LaunchedEffect(coinMarketCodes) {
        Log.d(TAG, "coinMarketCodes123")
        coinMarketCodes.let {
            val marketCodesGroup = it.onEach {
                Log.d(TAG,it.toString())
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
                        CoinGroup.KRW.name -> viewModel.setKrwGroupMarketCodes(value)
                        CoinGroup.BTC.name -> viewModel.setBtcGroupMarketCodes(value)
                        CoinGroup.USDT.name -> viewModel.setUsdtGroupMarketCodes(value)
                    }
                }
            }
            Log.d(TAG,"marketCodesGroup size ${marketCodesGroup.size}")

            viewModel.getRealTimeStock()
        }
    }

    LaunchedEffect(coinCurrentPrices) {
        Log.d(TAG,"selectedTabIndex $selectedTabIndex,coinCurrentPrices $coinCurrentPrices changed!!")
        if (!coinCurrentPrices.isNullOrEmpty()) {
            coinCurrentPriceForViewList.clear()
            coinCurrentPrices.forEach { coinCurrentPrice->
                coinCurrentPriceForViewList.add(CoinCurrentPriceForView(
                    market = coinCurrentPrice.market,
                    tradePrice = coinCurrentPrice.tradePrice,
                    prevClosingPrice = coinCurrentPrice.prevClosingPrice,
                    changePrice = coinCurrentPrice.changePrice,
                    change = coinCurrentPrice.change,
                    changeRate = coinCurrentPrice.changeRate,
                    accTradePrice24h = coinCurrentPrice.accTradePrice24h
                ))
            }

            viewModel.requestRealTimeCoinData(selectedTabIndex)
        }
    }

    LaunchedEffect(realTimeCoinCurrentPrice) {
        if (realTimeCoinCurrentPrice == null) return@LaunchedEffect

        if (realTimeCoinCurrentPrice?.market == "KRW-BTC") {
            Log.d("$funcName","${funcName}          (3)KRW-BTC 현재가 ${realTimeCoinCurrentPrice?.tradePrice?.let {
                DecimalFormat("#,###").format(it.toInt()).toString()
            } ?: ""} 전일대비 ${realTimeCoinCurrentPrice?.changeRate?.let{
                val changeSymbol = when(realTimeCoinCurrentPrice?.change) {
                    "RISE" -> "+"
                    "FALL" -> "-"
                    else -> ""
                }
                val changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(it * 100).let { result->
                    if (result == "0") "0.00" else result
                }
                "$changeSymbol${changeRate}%"
            } ?: ""} ${realTimeCoinCurrentPrice?.changePrice?.let {
                val changeSymbol = when(realTimeCoinCurrentPrice?.change) {
                    "FALL" -> "-"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: ""} 거래대금 ${realTimeCoinCurrentPrice?.accTradePrice24h?.let {
                val result = (it.toDouble() / 100000) * 0.1
                "${DecimalFormat("#,###").format(result.roundToInt()).toString()}백만"
            } ?: ""}")
        }

        coinCurrentPriceForViewList.forEach { coinCurrentPriceForView->
            if (realTimeCoinCurrentPrice != null && coinCurrentPriceForView.market == realTimeCoinCurrentPrice?.market) {
                coinCurrentPriceForView.tradePrice = realTimeCoinCurrentPrice?.tradePrice
                coinCurrentPriceForView.prevClosingPrice = realTimeCoinCurrentPrice?.prevClosingPrice
                coinCurrentPriceForView.changePrice = realTimeCoinCurrentPrice?.changePrice
                coinCurrentPriceForView.change = realTimeCoinCurrentPrice?.change
                coinCurrentPriceForView.changeRate = realTimeCoinCurrentPrice?.changeRate
                coinCurrentPriceForView.accTradePrice24h = realTimeCoinCurrentPrice?.accTradePrice24h
                coinCurrentPriceForView.isNewData = realTimeCoinCurrentPrice?.isNewData
                return@forEach
            }
        }
        // 여기까지 끝나면, Scaffold body 부터 다시 시작함
        Log.d(TAG,"LaunchedEffect(realTimeCoinCurrentPrice) end")
    }

    LaunchedEffect(Unit) {
        viewModel.sharedFlow.collect { coinData->
            //Log.d(TAG, "sendRealTimeCoinCurrentPriceToMain ${coinData.market}")
            if (coinData.market == "KRW-BTC") {
                Log.d("$funcName","$funcName          (2)KRW-BTC 현재가123 ${coinData.tradePrice?.let {
                    DecimalFormat("#,###").format(it.toInt()).toString()
                } ?: ""} 전일대비 ${coinData.changeRate?.let{
                    val changeSymbol = when(coinData.change) {
                        "RISE" -> "+"
                        "FALL" -> "-"
                        else -> ""
                    }
                    val changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(it * 100).let { result->
                        if (result == "0") "0.00" else result
                    }
                    "$changeSymbol${changeRate}%"
                } ?: ""} ${coinData.changePrice?.let {
                    val changeSymbol = when(coinData.change) {
                        "FALL" -> "-"
                        else -> ""
                    }
                    val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                        if (result == "0") "0.0000" else result
                    }
                    "$changeSymbol$changePrice"
                } ?: ""} 거래대금 ${coinData.accTradePrice24h?.let {
                    val result = (it.toDouble() / 100000) * 0.1
                    "${DecimalFormat("#,###").format(result.roundToInt()).toString()}백만"
                } ?: ""}")
            }
            //coinData까지는 정상 수신이 되나, 이를 compose state에 전달하면 state가 모두 받아내지 못하고 몇몇을 skip하고 있다..
            realTimeCoinCurrentPrice = coinData
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

@Composable
private fun CurrentPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView, viewModel: MainViewModel, onClick: (CoinMarketCode)->Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(start = 10.dp, end = 10.dp)
        .clickable {
            viewModel.getMarketCodes()?.find { it.market == coinCurrentPriceForView.market}?.let {
                onClick(it)
            }
        }
    )
    {
        val textColor: Color = when(coinCurrentPriceForView.change) {
            "RISE" -> Color.Red
            "FALL" -> Color.Blue
            else -> Color.Black
        }
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.25f, true)) {
            Text(text = coinCurrentPriceForView.market?.let{
                viewModel.getMarketName(it)
            } ?: "")
            Text(text = viewModel.getMarketCodeToDisplay(coinCurrentPriceForView.market))
        }

        Box(modifier = Modifier
            .border(
                width = 2.dp,
                color = if (coinCurrentPriceForView.isNewData == true) textColor else Color.Transparent,
                shape = RectangleShape
            )
            .weight(.25f, true)
            .fillMaxHeight())
        {
            Text(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 2.dp), textAlign = TextAlign.End, color = textColor, text = coinCurrentPriceForView.tradePrice?.let {
                DecimalFormat("#,###.####").format(it).toString()
            } ?: "")

        }
        if (coinCurrentPriceForView.isNewData == true) {
            coinCurrentPriceForView.isNewData = false
        }

        Column(horizontalAlignment = Alignment.End, modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.25f, true)) {
            Text(color = textColor, text = coinCurrentPriceForView.changeRate?.let{
                val changeSymbol = when(coinCurrentPriceForView.change) {
                    "RISE" -> "+"
                    "FALL" -> "-"
                    else -> ""
                }
                val changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(it * 100).let { result->
                    if (result == "0") "0.00" else result
                }
                "$changeSymbol${changeRate}%"
            } ?: "")
            Text(color = textColor, text = coinCurrentPriceForView.changePrice?.let {
                val changeSymbol = when(coinCurrentPriceForView.change) {
                    "FALL" -> "-"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: "")
        }
        Text(textAlign = TextAlign.End, modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.25f, true),
            text = coinCurrentPriceForView.accTradePrice24h?.let {
                val result = (it.toDouble() / 100000) * 0.1
                "${DecimalFormat("#,###").format(result.roundToInt()).toString()}백만"
            } ?: "")
    }
}

@Preview
@Composable
private fun MainPreview() {
    StockAppTheme {
        Surface {
            Main(rememberNavController(), hiltViewModel())
        }
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(rememberNavController())
}
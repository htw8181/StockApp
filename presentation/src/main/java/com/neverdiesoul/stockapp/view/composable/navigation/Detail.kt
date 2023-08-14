package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.neverdiesoul.data.repository.remote.websocket.UpbitRealTimeCoinOrderBookPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.model.CoinOrderBookPrice
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.viewmodel.BaseRealTimeViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.NONE_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.TabGroup
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import com.neverdiesoul.stockapp.viewmodel.model.CoinOrderbookUnitForDetailView
import java.math.RoundingMode
import java.text.DecimalFormat

private const val TAG = "NavDetailView"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController: NavHostController, viewModel: DetailViewModel, coinMarketCode: CoinMarketCode) {
    val context = LocalContext.current

    var selectedTabIndex by remember { mutableStateOf(NONE_STATE) }

    val realTimeCoinCurrentPrice by viewModel.coinCurrentPriceForViewSharedFlow.collectAsState(
        initial = CoinCurrentPriceForView()
    )

    val coinOrderBookPrices: List<CoinOrderBookPrice> by viewModel.coinOrderBookPrices.observeAsState(initial = mutableListOf())

    var realTimeCoinOrderBookPrice: UpbitRealTimeCoinOrderBookPrice by remember {
        mutableStateOf(UpbitRealTimeCoinOrderBookPrice())
    }

    val coinOrderBookAskPriceForDeatilViewList = remember {
        mutableStateListOf<CoinOrderbookUnitForDetailView>()
    }
    val coinOrderBookBidPriceForDeatilViewList = remember {
        mutableStateListOf<CoinOrderbookUnitForDetailView>()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                text = "${coinMarketCode.korean_name}(${viewModel.getMarketCodeToDisplay(coinMarketCode.market)})",
                fontSize = 25.sp,
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
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight())
            {
                Column(modifier = Modifier
                    .weight(0.5f, true)
                    .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)) {
                    CurrentPriceItem(realTimeCoinCurrentPrice)
                }

                Box(modifier = Modifier
                    .weight(0.5f, true)
                    .align(Alignment.CenterVertically)) {
                    Text(text = "Chart Area..", modifier = Modifier.align(Alignment.Center))
                }
            }
            Row {
                TabRow(selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
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
                LazyColumn(modifier = Modifier.weight(0.42f,true)) {
                    val onListItemClick = { coinOrderbookUnitForDetailView: CoinOrderbookUnitForDetailView->

                    }
                    val list = mutableListOf<CoinOrderbookUnitForDetailView>()
                    list.addAll(coinOrderBookAskPriceForDeatilViewList)
                    list.addAll(coinOrderBookBidPriceForDeatilViewList)

                    items(list) {
                        OrderBookPriceItem(realTimeCoinCurrentPrice, it, viewModel, onListItemClick)
                    }
                }

                OrderTabContent(modifier = Modifier
                    .weight(0.58f, true)
                    .fillMaxHeight()) {
                    TestComponent()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setViewEvent(object : BaseRealTimeViewModel.ViewEvent {
            override fun viewOnReady() {
                viewModel.getCoinOrderBookPriceFromRemote(listOf(coinMarketCode.market))
            }

            override fun viewOnExit() {
                Toast.makeText(context,"viewOnExit", Toast.LENGTH_SHORT).show()
                Log.d(TAG,"viewOnExit")
            }
        })
        viewModel.getRealTimeStock()

        viewModel.coinOrderBookPriceForViewSharedFlow.collect { coinOrderBookPrice ->
            realTimeCoinOrderBookPrice = coinOrderBookPrice
        }
    }

    LaunchedEffect(coinOrderBookPrices) {
        if (!coinOrderBookPrices.isNullOrEmpty()) {
            coinOrderBookAskPriceForDeatilViewList.clear()
            coinOrderBookBidPriceForDeatilViewList.clear()
            coinOrderBookPrices.forEach { coinOrderBookPrice->
                coinOrderBookPrice.orderbookUnits.forEach { coinOrderBookUnit->
                    coinOrderBookAskPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = true, price = coinOrderBookUnit.askPrice, size = coinOrderBookUnit.askSize))
                    coinOrderBookBidPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = false, price = coinOrderBookUnit.bidPrice, size = coinOrderBookUnit.bidSize))
                }
                coinOrderBookAskPriceForDeatilViewList.reverse()
            }

            viewModel.requestRealTimeCoinData(coinMarketCode)
        }
    }

    LaunchedEffect(realTimeCoinOrderBookPrice) {
        if (realTimeCoinOrderBookPrice == null) return@LaunchedEffect
        coinOrderBookAskPriceForDeatilViewList.clear()
        coinOrderBookBidPriceForDeatilViewList.clear()
        realTimeCoinOrderBookPrice.orderbookUnits.forEach { coinOrderBookUnit->
            coinOrderBookAskPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = true, price = coinOrderBookUnit.askPrice, size = coinOrderBookUnit.askSize))
            coinOrderBookBidPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = false, price = coinOrderBookUnit.bidPrice, size = coinOrderBookUnit.bidSize))
        }
        coinOrderBookAskPriceForDeatilViewList.reverse()
    }
}

@Composable
private fun CurrentPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView) {
    val textColor: Color = when(coinCurrentPriceForView.change) {
        "RISE" -> Color.Red
        "FALL" -> Color.Blue
        else -> Color.Black
    }
    Text(color = textColor, text = coinCurrentPriceForView.tradePrice?.let {
        DecimalFormat("#,###.####").format(it).toString()
    } ?: "", fontSize = 25.sp)
    Row {
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
        Text(color = textColor,
            text = coinCurrentPriceForView.changePrice?.let {
                val changeSymbol = when(coinCurrentPriceForView.change) {
                    "RISE" -> "▲"
                    "FALL" -> "▼"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: "",
            modifier = Modifier.padding(start = 10.dp))
    }
}

@Composable
private fun OrderBookPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView, coinOrderbookUnitForDetailView: CoinOrderbookUnitForDetailView, viewModel: DetailViewModel, onClick: (CoinOrderbookUnitForDetailView)->Unit) {
    val isCurrentPrice = coinCurrentPriceForView.tradePrice == coinOrderbookUnitForDetailView.price
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(
            if (coinOrderbookUnitForDetailView.isAsk) Color(
                red = 227,
                green = 235,
                blue = 245
            ) else Color(red = 251, green = 241, blue = 239)
        )
        .border(
            width = if (isCurrentPrice) 1.dp else 0.5.dp,
            color = if (isCurrentPrice) Color.Black else Color.White,
            shape = RectangleShape
        )
        .clickable {
            onClick(coinOrderbookUnitForDetailView)
        }
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End, modifier = Modifier
            //.align(Alignment.CenterVertically)
            .weight(.6f, true)
            .fillMaxHeight()
            .padding(start = 2.dp, end = 2.dp)) {

            /**
             * 변화율 계싼 : (호가 - 전일종가) / 전일종가
             */
            var changeRate = 0.0
            val changeRateStr = if ((coinCurrentPriceForView.prevClosingPrice != null && coinOrderbookUnitForDetailView.price != null)) {
                val prevClosingPrice = coinCurrentPriceForView.prevClosingPrice!!
                val price = coinOrderbookUnitForDetailView.price
                changeRate = ((price.minus(prevClosingPrice) ) / prevClosingPrice * 100)
                DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(changeRate).let { result->
                    if (result == "0") "0.00" else result
                } ?: "0.00"
            } else "0.00"

            val textColor = when {
                changeRate > 0 -> Color.Red
                changeRate < 0 -> Color.Blue
                else -> Color.Black
            }

            val changeSymbol = when {
                changeRate > 0 -> "+"
                changeRate < 0 -> "-"
                else -> ""
            }

            val orderBookPrice = coinOrderbookUnitForDetailView.price?.let {
                DecimalFormat("#,###").format(it.toInt()).toString()
            } ?: "0"
            Text(color = textColor,text = orderBookPrice)

            Text(color = textColor, text = "$changeSymbol${changeRateStr}%")
        }

        Spacer(modifier = Modifier
            .background(color = Color.White)
            .width(1.dp)
            .fillMaxHeight())

        Text(textAlign = TextAlign.Start, modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.4f, true),
            text = coinOrderbookUnitForDetailView.size?.let {
                val result = DecimalFormat("#,###.###").format(it).toString()
                if (result == "0") "0.000" else result
            } ?: "")
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
    Text(text = "content body2", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
private fun DetailPreview() {
    StockAppTheme {
        Surface {
            Detail(rememberNavController(), hiltViewModel(), CoinMarketCode(market = "KRW-BTC", korean_name = "비트코인", english_name = "BitCoin"))
        }
    }
}
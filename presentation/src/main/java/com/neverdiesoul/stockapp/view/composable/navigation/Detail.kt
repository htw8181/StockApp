package com.neverdiesoul.stockapp.view.composable.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.neverdiesoul.stockapp.view.composable.navigation.detail.DetailCurrentPriceItem
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBookPriceItem
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBottomSheet
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBuyTabContent
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderModalBottomSheetType
import com.neverdiesoul.stockapp.viewmodel.BaseRealTimeViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.BUY_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.NONE_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.SELL_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.TabGroup
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import com.neverdiesoul.stockapp.viewmodel.model.CoinOrderbookUnitForDetailView

private const val TAG = "NavDetailView"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController: NavHostController, viewModel: DetailViewModel, coinMarketCode: CoinMarketCode) {
    val context = LocalContext.current

    /**
     * 주문/호가/차트/시세/정보 탭 인덱스
     */
    var selectedTabIndex by remember { mutableStateOf(NONE_STATE) }

    /**
     * 주문 탭의 매수/매도/거래내역 탭 인덱스
     */
    var selectedOrderTabIndex by remember { mutableStateOf(BUY_STATE) }

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

    var showBottomSheetKeyboard by remember {
        mutableStateOf(false)
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
        Box(modifier = Modifier.fillMaxSize()) {
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
                        DetailCurrentPriceItem(realTimeCoinCurrentPrice)
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
                            OrderBookPriceItem(realTimeCoinCurrentPrice, it, onListItemClick)
                        }
                    }

                    OrderTabContent(modifier = Modifier
                        .weight(0.58f, true)
                        .fillMaxHeight(), selectedOrderTabIndex = selectedOrderTabIndex, onClick = { tabIndex -> selectedOrderTabIndex = tabIndex })
                    {
                        when(selectedOrderTabIndex) {
                            BUY_STATE -> OrderBuyTabContent(showBottomSheetToCalculate = { showBottomSheetKeyboard = true })
                            else -> TestComponent()
                        }
                    }
                }
            }

            val onClick = { showBottomSheetKeyboard = false }
            val onDismiss = { showBottomSheetKeyboard = false }
            OrderBottomSheet(modifier = Modifier.align(Alignment.BottomCenter),
                showBottomSheetKeyboard = showBottomSheetKeyboard,
                realTimeCoinCurrentPrice = realTimeCoinCurrentPrice,
                orderBookHogaPriceItemList = mutableListOf<CoinOrderbookUnitForDetailView>().apply {
                    this.addAll(coinOrderBookAskPriceForDeatilViewList)
                    this.addAll(coinOrderBookBidPriceForDeatilViewList)
                },
                style = OrderModalBottomSheetType.STYLE1,
                onClick = onClick,
                onDismiss = onDismiss)
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
                    coinOrderBookAskPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = true, price = coinOrderBookUnit.askPrice, size = coinOrderBookUnit.askSize, totalSize = coinOrderBookPrice.totalAskSize))
                    coinOrderBookBidPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = false, price = coinOrderBookUnit.bidPrice, size = coinOrderBookUnit.bidSize, totalSize = coinOrderBookPrice.totalBidSize))
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
            coinOrderBookAskPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = true, price = coinOrderBookUnit.askPrice, size = coinOrderBookUnit.askSize, totalSize = realTimeCoinOrderBookPrice.totalAskSize))
            coinOrderBookBidPriceForDeatilViewList.add(CoinOrderbookUnitForDetailView(isAsk = false, price = coinOrderBookUnit.bidPrice, size = coinOrderBookUnit.bidSize, totalSize = realTimeCoinOrderBookPrice.totalBidSize))
        }
        coinOrderBookAskPriceForDeatilViewList.reverse()
    }
}

@Composable
private fun OrderTabContent(modifier: Modifier, selectedOrderTabIndex: Int, onClick: (Int)->Unit, content: @Composable ()->Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        Row {
            TabRow(selectedTabIndex = selectedOrderTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                tabs = {
                    enumValues<DetailViewModel.OrderTabGroup>().forEachIndexed { tabIndex, tabValue ->
                        Tab(modifier = Modifier.background(color = if (tabIndex == selectedOrderTabIndex) Color.White else Color(red = 238, green = 238, blue = 238)),
                            selectedContentColor = if (tabIndex == selectedOrderTabIndex) when(selectedOrderTabIndex) {
                                BUY_STATE -> Color.Red
                                SELL_STATE -> Color.Blue
                                else -> Color.Black
                            } else Color(red = 51, green = 51, blue = 51),
                            selected = tabIndex == selectedOrderTabIndex,
                            onClick = {
                                onClick(tabIndex)
                            },
                            content = {
                                Column(modifier = Modifier.height(40.dp), verticalArrangement = Arrangement.Center) {
                                    Text(text = stringResource(id = tabValue.resId))
                                }
                            })
                    }
                },
                divider = {},
                indicator = {}
            )
        }

        content()
    }
}

@Composable
private fun TestComponent() {
    Text(text = "개발 예정", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
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
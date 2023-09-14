package com.neverdiesoul.stockapp.view.composable.navigation

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.neverdiesoul.stockapp.view.composable.navigation.detail.DetailBarChartView
import com.neverdiesoul.stockapp.view.composable.navigation.detail.DetailCandleStickChartView
import com.neverdiesoul.stockapp.view.composable.navigation.detail.DetailCurrentPriceItem
import com.neverdiesoul.stockapp.view.composable.navigation.detail.DetailLineChartView
import com.neverdiesoul.stockapp.view.composable.navigation.detail.HogaOrderView
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBookPriceItem
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBottomSheet
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBuyTabContent
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderModalBottomSheetType
import com.neverdiesoul.stockapp.viewmodel.BaseRealTimeViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.CandleDataRequestType
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.CandleDataRequestUnitType
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.BUY_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.CHART_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.HOGA_ORDER_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.ORDER_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.SELL_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.TabGroup
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import com.neverdiesoul.stockapp.viewmodel.model.CoinOrderbookUnitForDetailView
import java.util.*

private const val TAG = "NavDetailView"
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Detail(navController: NavHostController, viewModel: DetailViewModel, coinMarketCode: CoinMarketCode) {
    val context = LocalContext.current

    /**
     * 주문/호가/차트/시세/정보 탭 인덱스
     */
    var selectedTabIndex by remember { mutableStateOf(ORDER_STATE) }

    /**
     * 주문 탭의 매수/매도/거래내역 탭 인덱스
     */
    var selectedOrderTabIndex by remember { mutableStateOf(BUY_STATE) }

    /**
     * 분/일/주/월 차트 탭 인덱스
     */
    var selectedChartTabIndex by remember { mutableStateOf(CandleDataRequestType.DAYS.ordinal) }

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

    var isDropdownMenuExpandedForChartMinuteUnit by remember {
        mutableStateOf(false)
    }

    val onChartTabClick: (Int)->Unit = { index ->
        selectedChartTabIndex = index
        val type = when(selectedChartTabIndex) {
            CandleDataRequestType.MINUTE.ordinal -> CandleDataRequestType.MINUTE.value
            CandleDataRequestType.DAYS.ordinal -> CandleDataRequestType.DAYS.value
            CandleDataRequestType.WEEKS.ordinal -> CandleDataRequestType.WEEKS.value
            CandleDataRequestType.MONTHS.ordinal -> CandleDataRequestType.MONTHS.value
            else -> null
        }
        type?.let {
            when(type) {
                CandleDataRequestType.MINUTE.value -> isDropdownMenuExpandedForChartMinuteUnit = !isDropdownMenuExpandedForChartMinuteUnit
                else -> {
                    isDropdownMenuExpandedForChartMinuteUnit = false
                    viewModel.getCoinCandleChartDataFromRemote(type = it, unit = "", market = coinMarketCode.market, to = "", count = 200, convertingPriceUnit = "")
                }
            }
        }
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
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight())
                {
                    Column(modifier = Modifier
                        .weight(0.65f, true)
                        .wrapContentHeight()
                        .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)) {
                        DetailCurrentPriceItem(realTimeCoinCurrentPrice)
                    }

                    DetailLineChartView(modifier = Modifier
                        .weight(0.35f, true)
                        .padding(end = 10.dp), viewModel = viewModel)
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

                when(selectedTabIndex) {
                    ORDER_STATE -> {
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
                    HOGA_ORDER_STATE -> {
                        HogaOrderView(viewModel = viewModel, marketCode = coinMarketCode.market)
                    }
                    CHART_STATE -> {
                        Box {
                            var tabRowHeight by remember {
                                mutableStateOf(0.dp)
                            }
                           Column {
                               val selectedColor = Color(red = 9, green = 54, blue = 135)
                               val density = LocalDensity.current
                               TabRow(selectedTabIndex = selectedChartTabIndex,
                                   modifier = Modifier
                                       .fillMaxWidth(0.5f)
                                       .border(
                                           width = 1.dp,
                                           color = Color.Gray,
                                           shape = RectangleShape
                                       ).onSizeChanged {
                                           with(density) {
                                               tabRowHeight = it.height.toDp()
                                           }
                                       },
                                   containerColor = Color.White,
                                   tabs = {
                                       enumValues<CandleDataRequestType>().forEachIndexed { index, type ->
                                           Tab(modifier = Modifier
                                               .border(
                                                   width = 2.dp,
                                                   color = if (index == selectedChartTabIndex) selectedColor else Color.Transparent,
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
                                               selected = index == selectedChartTabIndex,
                                               onClick = {
                                                   onChartTabClick(index)
                                               },
                                               content = { Text(text = stringResource(id = type.tapNameResId), modifier = Modifier.padding(10.dp)) })
                                       }
                                   },
                                   indicator = {}
                               )
                               DetailCandleStickChartView(modifier = Modifier.weight(0.6f,true),viewModel = viewModel)
                               DetailBarChartView(modifier = Modifier.weight(0.4f,true),viewModel = viewModel)
                           }

                           if (isDropdownMenuExpandedForChartMinuteUnit) {
                               Column(verticalArrangement = Arrangement.Top, modifier = Modifier.wrapContentSize().background(Color.Transparent).padding(top = tabRowHeight)) {
                                   TabRow(selectedTabIndex = -1,
                                       modifier = Modifier
                                           .wrapContentWidth()
                                           .background(Color.Blue)
                                           .border(
                                               width = 1.dp,
                                               color = Color.Gray,
                                               shape = RectangleShape
                                           ),
                                       containerColor = Color.White,
                                       tabs = {
                                           enumValues<CandleDataRequestUnitType>().forEachIndexed { index, value ->
                                               Tab(modifier = Modifier
                                                   .border(
                                                       width = 2.dp,
                                                       color = Color.Transparent,
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
                                                   unselectedContentColor = Color.Gray,
                                                   selected = false,
                                                   onClick = {
                                                       viewModel.getCoinCandleChartDataFromRemote(type = CandleDataRequestType.MINUTE.value, unit = value.unit, market = coinMarketCode.market, to = "", count = 200, convertingPriceUnit = "")
                                                       isDropdownMenuExpandedForChartMinuteUnit = false
                                                   },
                                                   content = { Text(text = "${value.unit}${stringResource(id = R.string.minute)}", modifier = Modifier.padding(10.dp)) })
                                           }
                                       },
                                       indicator = {}
                                   )
                               }
                           }
                        }
                    }
                    else -> {
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                            Text(text = "개발 예정")
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
                viewModel.getCoinLineChartDataFromRemote(market = coinMarketCode.market, to = "", count = 200, convertingPriceUnit = "")
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

    LaunchedEffect(selectedTabIndex) {
        when(selectedTabIndex) {
            CHART_STATE -> {
                onChartTabClick(selectedChartTabIndex)
            }
            else -> {}
        }
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
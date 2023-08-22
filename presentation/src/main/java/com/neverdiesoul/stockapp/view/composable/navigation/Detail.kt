package com.neverdiesoul.stockapp.view.composable.navigation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Paint
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.neverdiesoul.data.repository.remote.websocket.UpbitRealTimeCoinOrderBookPrice
import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.model.CoinOrderBookPrice
import com.neverdiesoul.stockapp.R
import com.neverdiesoul.stockapp.databinding.DetailChartTabViewBinding
import com.neverdiesoul.stockapp.databinding.DetailHogaTabViewBinding
import com.neverdiesoul.stockapp.ui.theme.StockAppTheme
import com.neverdiesoul.stockapp.view.composable.navigation.detail.DetailCurrentPriceItem
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBookPriceItem
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBottomSheet
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBuyTabContent
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderModalBottomSheetType
import com.neverdiesoul.stockapp.viewmodel.BaseRealTimeViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.CandleDataRequestType
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.BUY_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.CHART_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.HOGA_ORDER_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.ORDER_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.Companion.SELL_STATE
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel.TabGroup
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import com.neverdiesoul.stockapp.viewmodel.model.CoinOrderbookUnitForDetailView
import java.util.*

typealias ChartColor = android.graphics.Color

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
                        var backEnabled by remember {
                            mutableStateOf(false)
                        }
                        var webView: WebView? by remember {
                            mutableStateOf(null)
                        }
                        AndroidViewBinding(factory = DetailHogaTabViewBinding::inflate, modifier = Modifier.fillMaxSize()) {
                            Log.d(TAG ,"AndroidViewBinding DetailHogaTabFragmentBinding")
                            if (webView == null) {
                                Log.d("onPageStarted" ,"AndroidViewBinding Update")
                                webviewDetailHoga.apply {
                                    webViewClient = object : WebViewClient() {

                                        override fun shouldOverrideUrlLoading(
                                            view: WebView,
                                            request: WebResourceRequest?
                                        ): Boolean {
                                            Log.d("onPageStarted","shouldOverrideUrlLoading ${view.url}")
                                            return super.shouldOverrideUrlLoading(view, request)
                                        }

                                        override fun onPageStarted(
                                            view: WebView,
                                            url: String?,
                                            favicon: Bitmap?
                                        ) {
                                            progressBar.visibility = View.VISIBLE
                                            backEnabled = view.canGoBack()
                                            Log.d("onPageStarted" ,"onPageStarted backEnable $backEnabled")
                                            super.onPageStarted(view, url, favicon)
                                        }

                                        override fun onPageFinished(view: WebView, url: String?) {
                                            Log.d("onPageStarted" ,"onPageFinished")
                                            progressBar.visibility = View.GONE
                                            super.onPageFinished(view, url)
                                        }
                                    }
                                    webChromeClient = WebChromeClient()
                                    val webViewSetting = this.settings
                                    webViewSetting.apply {
                                        javaScriptEnabled = true; //웹뷰에서 javascript를 사용하도록 설정
                                        //javaScriptCanOpenWindowsAutomatically = false; //멀티윈도우 띄우는 것
                                        //loadWithOverviewMode = true; // 메타태그
                                        //useWideViewPort = true; //화면 사이즈 맞추기
                                        //setSupportZoom(true); // 화면 줌 사용 여부
                                        //builtInZoomControls = true; //화면 확대 축소 사용 여부
                                        //displayZoomControls = true; //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
                                        //cacheMode = WebSettings.LOAD_NO_CACHE; // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
                                        //defaultFixedFontSize = 14; //기본 고정 글꼴 크기, value : 1~72 사이의 숫자
                                    }
                                    //addJavascriptInterface(Bridge(),"StockAppWebViewBridge")
                                    loadUrl("https://htw8181.github.io/")
                                    webView = this
                                    backEnabled = true
                                }
                            }

                        }
                        // 뒤로가기 이벤트가 감지 됐을 때 enabled이 true면 onBack()을 실행하게 된다.
                        BackHandler(enabled = backEnabled, onBack = {
                            webView?.goBack()
                        })
                    }
                    CHART_STATE -> {
                        val coinCandleChartDataList: List<CoinCandleChartData> by viewModel.coinCandleChartDataList.observeAsState(initial = mutableListOf())
                        AndroidViewBinding(factory = DetailChartTabViewBinding::inflate, modifier = Modifier.fillMaxSize()) {
                            Log.d(TAG,"AndroidViewBinding DetailChartTabViewBinding Candle Chart!!")
                            if (coinCandleChartDataList.isNullOrEmpty()) return@AndroidViewBinding
                            val entries = ArrayList<CandleEntry>()
                            coinCandleChartDataList.forEachIndexed { index, coinCandleChartData ->
                                entries.add(
                                    CandleEntry(index.toFloat(),
                                        coinCandleChartData.highPrice?.toFloat() ?: 0.0f,
                                        coinCandleChartData.lowPrice?.toFloat() ?: 0.0f,
                                        coinCandleChartData.openingPrice?.toFloat()  ?: 0.0f,
                                        coinCandleChartData.tradePrice?.toFloat() ?: 0.0f)
                                )
                            }

                            val dataSet = CandleDataSet(entries, "").apply {
                                // 심지 부분
                                shadowColor = ChartColor.LTGRAY
                                shadowWidth = 1F

                                // 음봄
                                decreasingColor = ChartColor.BLUE
                                decreasingPaintStyle = Paint.Style.FILL
                                // 양봉
                                increasingColor = ChartColor.RED
                                increasingPaintStyle = Paint.Style.FILL

                                neutralColor = ChartColor.DKGRAY
                                setDrawValues(false)
                                // 터치시 노란 선 제거
                                //highLightColor = ChartColor.TRANSPARENT
                            }

                            // 왼쪽 Y 축
                            chartCandleStick.axisLeft.run {
                                this.isEnabled = false
                            }

                            // 오른쪽 Y 축
                            chartCandleStick.axisRight.run {
                                isEnabled = true
                            }

                            // X 축
                            chartCandleStick.xAxis.run {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawAxisLine(true)
                                setDrawGridLines(true)
                                setLabelCount(5,true)
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return  coinCandleChartDataList[value.toInt()].candleDateTimeKst?.substring(0,10) ?: "empty"
                                    }
                                }
                            }

                            // 범례
                            chartCandleStick.legend.run {
                                isEnabled = false
                            }
                            chartCandleStick.setMaxVisibleValueCount(100)
                            chartCandleStick.apply {
                                this.data = CandleData(dataSet)
                                setDrawBorders(true)
                                isScaleYEnabled = false
                                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                                        Log.d(TAG,"$e , $h");

                                    }

                                    override fun onNothingSelected() {

                                    }
                                })
                                description.isEnabled = false
                                //isHighlightPerDragEnabled = false
                                //isHighlightPerTapEnabled = false

                                //requestDisallowInterceptTouchEvent(true)
                                invalidate()
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
                viewModel.getCoinCandleChartDataFromRemote(type = CandleDataRequestType.DAYS.value, unit = "", market = coinMarketCode.market, to = "", count = 200, convertingPriceUnit = "")
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
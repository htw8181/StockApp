package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.neverdiesoul.data.repository.remote.websocket.RealTimeDataType
import com.neverdiesoul.data.repository.remote.websocket.UpbitRealTimeCoinCurrentPrice
import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.model.UpbitType
import com.neverdiesoul.domain.usecase.GetCoinCurrentPriceFromRemoteUseCase
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllFromLocalUseCase
import com.neverdiesoul.domain.usecase.RequestRealTimeCoinDataUseCase
import com.neverdiesoul.domain.usecase.TryConnectionToGetRealTimeCoinDataUseCase
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okio.ByteString
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class MainViewModel @Inject constructor(
        private val getCoinMarketCodeAllFromLocalUseCase: GetCoinMarketCodeAllFromLocalUseCase,
        private val getCoinCurrentPriceFromRemoteUseCase: GetCoinCurrentPriceFromRemoteUseCase,
        tryConnectionToGetRealTimeCoinDataUseCase: TryConnectionToGetRealTimeCoinDataUseCase,
        requestRealTimeCoinDataUseCase: RequestRealTimeCoinDataUseCase
    ) : BaseRealTimeViewModel(tryConnectionToGetRealTimeCoinDataUseCase,requestRealTimeCoinDataUseCase) {

    enum class CoinGroup {
        KRW,BTC,USDT
    }
    companion object {
        val NONE_STATE = -1
        val KRW_STATE = CoinGroup.KRW.ordinal
        val BTC_STATE = CoinGroup.BTC.ordinal
        val USDT_STATE = CoinGroup.USDT.ordinal
    }

    private var _coinMarketCodes: MutableLiveData<List<CoinMarketCode>> = MutableLiveData(mutableListOf())
    val coinMarketCodes: LiveData<List<CoinMarketCode>> = _coinMarketCodes

    private var _coinCurrentPrices: MutableLiveData<List<CoinCurrentPrice>> = MutableLiveData(mutableListOf())
    val coinCurrentPrices: LiveData<List<CoinCurrentPrice>> = _coinCurrentPrices

    private val _sharedFlow = MutableSharedFlow<CoinCurrentPriceForView>(
        /*replay = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST*/
    )
    val sharedFlow = _sharedFlow.asSharedFlow()

    private var krwGroupMarketCodes = listOf<CoinMarketCode>()
    private var btcGroupMarketCodes = listOf<CoinMarketCode>()
    private var usdtGroupMarketCodes = listOf<CoinMarketCode>()

    /**
     * 실시간 데이터가 연속으로 똑같은게 들어올때가 있어서 ,이전 데이터를 저장했다가 새로 데이터가 들어올때 비교해서 다를때에만 emit하도록 함
     */
    private var previousRealTimeCoinCurrentPrice = CoinCurrentPriceForView()
    
    fun setKrwGroupMarketCodes(marketCodes: List<CoinMarketCode>) {
        this.krwGroupMarketCodes = marketCodes
    }
    fun setBtcGroupMarketCodes(marketCodes: List<CoinMarketCode>) {
        this.btcGroupMarketCodes = marketCodes
    }
    fun setUsdtGroupMarketCodes(marketCodes: List<CoinMarketCode>) {
        this.usdtGroupMarketCodes = marketCodes
    }

    fun getMarketCodes() = this.coinMarketCodes.value

    override fun sendRealTimeCoinDataToView(bytes: ByteString) {
        val funcName = object{}.javaClass.enclosingMethod?.name

        val data = Gson().fromJson(bytes.string(Charsets.UTF_8), UpbitRealTimeCoinCurrentPrice::class.java)
        Log.d(tag,"수신 data => $data")

        Log.d(tag, "$funcName ${data.code}")
        var logMsg = "Empty"
        /*if (data.code == "KRW-BTC") {
            logMsg = "${MainViewModel::class.simpleName} (1)KRW-BTC 현재가 ${data.tradePrice?.let {
                DecimalFormat("#,###").format(it.toInt()).toString()
            } ?: ""} 전일대비 ${data.changeRate?.let{
                val changeSymbol = when(data.change) {
                    "RISE" -> "+"
                    "FALL" -> "-"
                    else -> ""
                }
                val changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(it * 100).let { result->
                    if (result == "0") "0.00" else result
                }
                "$changeSymbol${changeRate}%"
            } ?: ""} ${data.changePrice?.let {
                val changeSymbol = when(data.change) {
                    "FALL" -> "-"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: ""} 거래대금 ${data.accTradePrice24h?.let {
                val result = (it.toDouble() / 100000) * 0.1
                "${DecimalFormat("#,###").format(result.roundToInt()).toString()}백만"
            } ?: ""}"
            Log.d(tag,logMsg)
        }*/

        val coinCurrentPriceForView = CoinCurrentPriceForView(
            market = data.code,
            tradePrice = data.tradePrice,
            prevClosingPrice = data.prevClosingPrice,
            changeRate = data.changeRate,
            change = data.change,
            changePrice = data.changePrice,
            accTradePrice24h = data.accTradePrice24h,
            isNewData = true
        )

        if (data.code == "KRW-BTC") {
            logMsg = "${MainViewModel::class.simpleName} (1)KRW-BTC 현재가 ${data.tradePrice?.let {
                DecimalFormat("#,###").format(it.toInt()).toString()
            } ?: ""} 전일대비 ${data.changeRate?.let{
                val changeSymbol = when(data.change) {
                    "RISE" -> "+"
                    "FALL" -> "-"
                    else -> ""
                }
                val changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(it * 100).let { result->
                    if (result == "0") "0.00" else result
                }
                "$changeSymbol${changeRate}%"
            } ?: ""} ${data.changePrice?.let {
                val changeSymbol = when(data.change) {
                    "FALL" -> "-"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: ""} 거래대금 ${data.accTradePrice24h?.let {
                val result = (it.toDouble() / 100000) * 0.1
                "${DecimalFormat("#,###").format(result.roundToInt()).toString()}백만"
            } ?: ""}"
            Log.d(tag,logMsg)
        }

        // 데이터 클래스끼리는 == 으로 비교해도 주생성자로 받은 프로퍼티끼리(클래스 자료형은 제외) 구조적 동등성을 비교한다.
        if (previousRealTimeCoinCurrentPrice == coinCurrentPriceForView) {
            if (data.code == "KRW-BTC") Log.d(tag, "${MainViewModel::class.simpleName} (1)${coinCurrentPriceForView.market}  현재가 coinCurrentPriceForMainView == previousRealTimeCoinCurrentPrice")
            //Log.d(tag, "${MainViewModel::class.simpleName} (1)${coinCurrentPriceForMainView.market}  현재가 $coinCurrentPriceForMainView == $previousRealTimeCoinCurrentPrice")
            return
        }

        previousRealTimeCoinCurrentPrice = coinCurrentPriceForView

        viewModelScope.launch {
            _sharedFlow.emit(coinCurrentPriceForView)
        }
    }

    fun getCoinCurrentPrice(selectedTabIndex: Int) {
        when(selectedTabIndex) {
            KRW_STATE -> {
                getCoinCurrentPriceFromRemote(krwGroupMarketCodes.map { it.market })
            }
            BTC_STATE -> {
                getCoinCurrentPriceFromRemote(btcGroupMarketCodes.map { it.market })
            }
            USDT_STATE -> {
                getCoinCurrentPriceFromRemote(usdtGroupMarketCodes.map { it.market })
            }
        }
    }

    fun getCoinMarketCodeAllFromLocal() {
        val funcName = object{}.javaClass.enclosingMethod?.name
        viewModelScope.launch {
            getCoinMarketCodeAllFromLocalUseCase()
                .onStart { Log.d(tag,"$funcName onStart")  }
                .onCompletion { Log.d(tag,"$funcName onCompletion") }
                .catch { Log.d(tag,"Error!! $funcName -> $it") }
                .collect {
                    Log.d(tag,"$funcName collect size ${it.size}")
                    _coinMarketCodes.value = it
                }
        }
    }

    private fun getCoinCurrentPriceFromRemote(markets: List<String>) {
        val funcName = object{}.javaClass.enclosingMethod?.name
        viewModelScope.launch {
            getCoinCurrentPriceFromRemoteUseCase(markets)
                .onStart { Log.d(tag,"$funcName onStart")  }
                .onCompletion { Log.d(tag,"$funcName onCompletion") }
                .catch { Log.d(tag,"Error!! $funcName -> $it") }
                .collect { coinCurrentPriceList ->
                    coinCurrentPriceList.forEach { coinCurrentPrice ->
                        Log.d(tag, "$funcName collect data -> $coinCurrentPrice")
                    }
                    _coinCurrentPrices.value = coinCurrentPriceList.sortedByDescending { it.accTradePrice24h }
                }
        }
    }

    fun getMarketName(marketCode: String): String {
        coinMarketCodes.value?.forEach { coinMarketCode ->
            if (marketCode == coinMarketCode.market) {
                return coinMarketCode.korean_name
            }
        }
        return ""
    }

    fun requestRealTimeCoinData(selectedTabIndex: Int) {
        val dataType = RealTimeDataType.TICKER.type
        when(selectedTabIndex) {
            KRW_STATE -> {
                requestRealTimeCoinDataUseCase(listOf(UpbitType(dataType, krwGroupMarketCodes.map { it.market })))
            }
            BTC_STATE -> {
                requestRealTimeCoinDataUseCase(listOf(UpbitType(dataType, btcGroupMarketCodes.map { it.market })))
            }
            USDT_STATE -> {
                requestRealTimeCoinDataUseCase(listOf(UpbitType(dataType, usdtGroupMarketCodes.map { it.market })))
            }
        }
    }
}
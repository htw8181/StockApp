package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.neverdiesoul.data.repository.remote.websocket.UpbitWebSocketResponseData
import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.usecase.GetCoinCurrentPriceFromRemoteUseCase
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllFromLocalUseCase
import com.neverdiesoul.domain.usecase.RequestRealTimeCoinDataUseCase
import com.neverdiesoul.domain.usecase.TryConnectionToGetRealTimeCoinDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.math.BigDecimal
import javax.inject.Inject

enum class CoinGroup {
    KRW,BTC,USDT
}

val NONE_STATE = -1
val KRW_STATE = CoinGroup.KRW.ordinal
val BTC_STATE = CoinGroup.BTC.ordinal
val USDT_STATE = CoinGroup.USDT.ordinal

@HiltViewModel
class MainViewModel @Inject constructor(
        private val getCoinMarketCodeAllFromLocalUseCase: GetCoinMarketCodeAllFromLocalUseCase,
        private val getCoinCurrentPriceFromRemoteUseCase: GetCoinCurrentPriceFromRemoteUseCase,
        private val tryConnectionToGetRealTimeCoinDataUseCase: TryConnectionToGetRealTimeCoinDataUseCase,
        private val requestRealTimeCoinDataUseCase: RequestRealTimeCoinDataUseCase
    ) : ViewModel() {
    private val tag = this::class.simpleName

    private var _coinMarketCodes: MutableLiveData<List<CoinMarketCode>> = MutableLiveData(mutableListOf())
    val coinMarketCodes: LiveData<List<CoinMarketCode>> = _coinMarketCodes

    private var _coinCurrentPrices: MutableLiveData<List<CoinCurrentPrice>> = MutableLiveData(mutableListOf())
    val coinCurrentPrices: LiveData<List<CoinCurrentPrice>> = _coinCurrentPrices

    private var _realTimeCoinCurrentPrice: MutableLiveData<CoinCurrentPriceForMainView> = MutableLiveData(CoinCurrentPriceForMainView())
    val realTimeCoinCurrentPrice: LiveData<CoinCurrentPriceForMainView> = _realTimeCoinCurrentPrice

    var webSocket: WebSocket? = null

    private var krwGroupMarketCodes = listOf<CoinMarketCode>()
    private var btcGroupMarketCodes = listOf<CoinMarketCode>()
    private var usdtGroupMarketCodes = listOf<CoinMarketCode>()

    fun setKrwGroupMarketCodes(marketCodes: List<CoinMarketCode>) {
        this.krwGroupMarketCodes = marketCodes
    }
    fun setBtcGroupMarketCodes(marketCodes: List<CoinMarketCode>) {
        this.btcGroupMarketCodes = marketCodes
    }
    fun setUsdtGroupMarketCodes(marketCodes: List<CoinMarketCode>) {
        this.usdtGroupMarketCodes = marketCodes
    }

    init {

    }

    fun getRealTimeStock() {
        tryConnectionToGetRealTimeCoinDataUseCase.setWebSocketListener(RealTimeStockListener(this))
        tryConnectionToGetRealTimeCoinDataUseCase()
    }
    private class RealTimeStockListener(val viewModel: MainViewModel) : WebSocketListener() {
        private val tag = this::class.simpleName
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d(tag,"onClosed")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d(tag,"onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(tag,"onFailure ${response?.code}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(tag,"수신 text=> $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d(tag,"수신 bytes=> $bytes")
            try {
                val res = Gson().fromJson(bytes.string(Charsets.UTF_8),UpbitWebSocketResponseData::class.java)
                Log.d(tag,"수신 bytes=> $res")
                viewModel.sendRealTimeCoinCurrentPriceToMain(res)
            } catch (e: Exception) {
                Log.d(tag,e.message.toString())
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            viewModel.webSocket = webSocket
        }
    }

    fun sendRealTimeCoinCurrentPriceToMain(data: UpbitWebSocketResponseData) {
        Log.d(tag, "sendRealTimeCoinCurrentPriceToMain ${data.code}")
        _realTimeCoinCurrentPrice.postValue(CoinCurrentPriceForMainView(
            market = data.code,
            tradePrice = data.tradePrice,
            changeRate = data.changeRate,
            change = data.change,
            changePrice = data.changePrice,
            accTradePrice24h = data.accTradePrice24h
        ))
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

    override fun onCleared() {
        tryConnectionToGetRealTimeCoinDataUseCase.closeRealTimeStock()
        Log.d(tag,"RealTimeStock 통신 닫힘")

        super.onCleared()
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
                        Log.d(tag, "$funcName collect data -> ${coinCurrentPrice.toString()}")
                    }
                    _coinCurrentPrices.value = coinCurrentPriceList
                }
        }
    }

    /**
     * 메인 화면 리스트에 보여줄 코인 현재가 정보
     */
    data class CoinCurrentPriceForMainView(
        /**
         * 종목 구분 코드
         */
        val market             : String? = null,
        /**
         * 종가(현재가)
         */
        var tradePrice         : Double?    = null,
        /**
         * 변화율의 절대값
         */
        var changeRate         : Double? = null,
        /**
         * EVEN : 보합, RISE : 상승, FALL : 하락
         */
        var change             : String? = null,
        /**
         * 변화액의 절대값
         */
        var changePrice        : BigDecimal?    = null,
        /**
         * 24시간 누적 거래대금
         */
        var accTradePrice24h   : BigDecimal? = null,
    )

    fun getMarketName(marketCode: String): String {
        coinMarketCodes.value?.forEach { coinMarketCode ->
            if (marketCode == coinMarketCode.market) {
                return coinMarketCode.korean_name
            }
        }
        return ""
    }

    fun requestRealTimeCoinData(selectedTabIndex: Int) {
        when(selectedTabIndex) {
            KRW_STATE -> {
                requestRealTimeCoinDataUseCase(krwGroupMarketCodes)
            }
            BTC_STATE -> {
                requestRealTimeCoinDataUseCase(btcGroupMarketCodes)
            }
            USDT_STATE -> {
                requestRealTimeCoinDataUseCase(usdtGroupMarketCodes)
            }
        }
    }
}
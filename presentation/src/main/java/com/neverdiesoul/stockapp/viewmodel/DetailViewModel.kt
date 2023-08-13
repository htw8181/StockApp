package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.neverdiesoul.data.repository.remote.websocket.RealTimeDataType
import com.neverdiesoul.data.repository.remote.websocket.UpbitRealTimeCoinOrderBookPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.model.CoinOrderBookPrice
import com.neverdiesoul.domain.usecase.GetCoinOrderBookPriceFromRemoteUseCase
import com.neverdiesoul.domain.usecase.RequestRealTimeCoinDataUseCase
import com.neverdiesoul.domain.usecase.TryConnectionToGetRealTimeCoinDataUseCase
import com.neverdiesoul.stockapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okio.ByteString
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCoinOrderBookPriceFromRemoteUseCase: GetCoinOrderBookPriceFromRemoteUseCase,
    tryConnectionToGetRealTimeCoinDataUseCase: TryConnectionToGetRealTimeCoinDataUseCase,
    requestRealTimeCoinDataUseCase: RequestRealTimeCoinDataUseCase
) : BaseRealTimeViewModel(tryConnectionToGetRealTimeCoinDataUseCase,requestRealTimeCoinDataUseCase) {

    private var _coinOrderBookPrices: MutableLiveData<List<CoinOrderBookPrice>> = MutableLiveData(mutableListOf())
    val coinOrderBookPrices: LiveData<List<CoinOrderBookPrice>> = _coinOrderBookPrices

    private val _sharedFlow = MutableSharedFlow<UpbitRealTimeCoinOrderBookPrice>(
        /*replay = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST*/
    )
    val sharedFlow = _sharedFlow.asSharedFlow()

    enum class TabGroup(val resId: Int) {
        ORDER(R.string.detail_tab_order),
        HOGA_ORDER(R.string.detail_tab_hoga_order),
        CHART(R.string.detail_tab_chart),
        SISE(R.string.detail_tab_sise),
        INFO(R.string.detail_tab_info)
    }
    companion object {
        val NONE_STATE = -1
        val ORDER_STATE = TabGroup.ORDER.ordinal
        val HOGA_ORDER_STATE = TabGroup.HOGA_ORDER.ordinal
        val CHART_STATE = TabGroup.CHART.ordinal
        val SISE_STATE = TabGroup.SISE.ordinal
        val INFO_STATE = TabGroup.INFO.ordinal
    }

    fun getCoinOrderBookPriceFromRemote(markets: List<String>) {
        val funcName = object{}.javaClass.enclosingMethod?.name
        viewModelScope.launch {
            getCoinOrderBookPriceFromRemoteUseCase(markets)
                .onStart { Log.d(tag,"$funcName onStart")  }
                .onCompletion { Log.d(tag,"$funcName onCompletion") }
                .catch { Log.d(tag,"Error!! $funcName -> $it") }
                .collect { coinOrderBookPricesList ->
                    coinOrderBookPricesList.forEach { coinOrderBookPrice ->
                        Log.d(tag, "$funcName collect data -> $coinOrderBookPrice")
                    }
                    _coinOrderBookPrices.value = coinOrderBookPricesList
                }
        }
    }

    override fun sendRealTimeCoinDataToView(bytes: ByteString) {
        val funcName = object{}.javaClass.enclosingMethod?.name

        val data = Gson().fromJson(bytes.string(Charsets.UTF_8), UpbitRealTimeCoinOrderBookPrice::class.java)
        Log.d(tag,"DetailViewModel 수신 data => $data")

        Log.d(tag, "$funcName ${data.code}")

        viewModelScope.launch {
            _sharedFlow.emit(data)
        }
    }

    fun requestRealTimeCoinData(coinMarketCode: CoinMarketCode) {
        requestRealTimeCoinDataUseCase(RealTimeDataType.ORDERBOOK.type, listOf(coinMarketCode))
    }

    /**
     * 상세 화면 리스트에 보여줄 코인 호가 정보
     */
    data class OrderbookUnitForDeatilView (
        /**
         * true: 매도, false: 매수
         */
        val isAsk: Boolean = true,
        /**
         * 호가
         */
        val price: Double? = null,
        /**
         * 잔량
         */
        val size: Double? = null
    )
}
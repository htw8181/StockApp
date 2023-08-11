package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import com.google.gson.Gson
import com.neverdiesoul.data.repository.remote.websocket.RealTimeDataType
import com.neverdiesoul.data.repository.remote.websocket.UpbitRealTimeCoinOrderBookPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.usecase.RequestRealTimeCoinDataUseCase
import com.neverdiesoul.domain.usecase.TryConnectionToGetRealTimeCoinDataUseCase
import com.neverdiesoul.stockapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.ByteString
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    tryConnectionToGetRealTimeCoinDataUseCase: TryConnectionToGetRealTimeCoinDataUseCase,
    requestRealTimeCoinDataUseCase: RequestRealTimeCoinDataUseCase
) : BaseRealTimeViewModel(tryConnectionToGetRealTimeCoinDataUseCase,requestRealTimeCoinDataUseCase) {

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

    override fun sendRealTimeCoinDataToView(bytes: ByteString) {
        val funcName = object{}.javaClass.enclosingMethod?.name

        val data = Gson().fromJson(bytes.string(Charsets.UTF_8), UpbitRealTimeCoinOrderBookPrice::class.java)
        Log.d(tag,"DetailViewModel 수신 data => $data")

        Log.d(tag, "$funcName ${data.code}")
    }

    fun requestRealTimeCoinData(coinMarketCode: CoinMarketCode) {
        requestRealTimeCoinDataUseCase(RealTimeDataType.ORDERBOOK.type, listOf(coinMarketCode))
    }
}
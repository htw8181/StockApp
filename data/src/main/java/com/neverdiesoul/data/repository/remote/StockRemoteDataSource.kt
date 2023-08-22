package com.neverdiesoul.data.repository.remote

import com.neverdiesoul.data.model.ResponseCoinCandleChartData
import com.neverdiesoul.data.model.ResponseCoinCurrentPrice
import com.neverdiesoul.data.model.ResponseCoinMarketCode
import com.neverdiesoul.data.model.ResponseCoinOrderBookPrice
import com.neverdiesoul.domain.model.UpbitType
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRemoteDataSource {
    fun getCoinMarketCodeAllFromRemote(): Flow<List<ResponseCoinMarketCode>>
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
    fun requestRealTimeCoinData(upbitTypeList: List<UpbitType>)
    fun getCoinCurrentPriceFromRemote(markets: List<String>): Flow<List<ResponseCoinCurrentPrice>>
    fun getCoinOrderBookPriceFromRemote(markets: List<String>): Flow<List<ResponseCoinOrderBookPrice>>
    fun getCoinCandleChartDataFromRemote(type: String, unit: String, market: String, to: String, count: Int, convertingPriceUnit: String): Flow<List<ResponseCoinCandleChartData>>
}
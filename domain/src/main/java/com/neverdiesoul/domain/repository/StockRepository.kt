package com.neverdiesoul.domain.repository

import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.model.CoinOrderBookPrice
import com.neverdiesoul.domain.model.UpbitType
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRepository {
    fun getCoinMarketCodeAllFromRemote(): Flow<Boolean>
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
    fun getCoinMarketCodeAllFromLocal(): Flow<List<CoinMarketCode>>
    fun requestRealTimeCoinData(upbitTypeList: List<UpbitType>)
    fun getCoinCurrentPriceFromRemote(markets: List<String>): Flow<List<CoinCurrentPrice>>
    fun getCoinOrderBookPriceFromRemote(markets: List<String>): Flow<List<CoinOrderBookPrice>>
    fun getCoinCandleChartDataFromRemote(type: String, unit: String, market: String, to: String, count: Int, convertingPriceUnit: String): Flow<List<CoinCandleChartData>>
}
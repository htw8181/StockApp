package com.neverdiesoul.domain.repository

import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRepository {
    fun getCoinMarketCodeAllFromRemote(): Flow<Boolean>
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
    fun getCoinMarketCodeAllFromLocal(): Flow<List<CoinMarketCode>>
    fun requestRealTimeCoinData(dataType: String, marketCodes: List<CoinMarketCode>)
    fun getCoinCurrentPriceFromRemote(markets: List<String>): Flow<List<CoinCurrentPrice>>
}
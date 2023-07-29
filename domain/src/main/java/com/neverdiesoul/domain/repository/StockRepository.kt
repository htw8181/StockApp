package com.neverdiesoul.domain.repository

import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRepository {
    fun getCoinMarketCodeAll(): Flow<List<CoinMarketCode>>
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
}
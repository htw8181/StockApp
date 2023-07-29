package com.neverdiesoul.data.repository.remote

import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRemoteDataSource {
    fun getCoinMarketCodeAll(): Flow<List<CoinMarketCode>>
    fun getRealTimeStock(webSocketListener: WebSocketListener)

    fun closeRealTimeStock()
}
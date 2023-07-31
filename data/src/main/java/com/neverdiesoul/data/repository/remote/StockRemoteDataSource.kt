package com.neverdiesoul.data.repository.remote

import com.neverdiesoul.data.model.ResponseCoinMarketCode
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRemoteDataSource {
    fun getCoinMarketCodeAll(): Flow<List<ResponseCoinMarketCode>>
    fun getRealTimeStock(webSocketListener: WebSocketListener)

    fun closeRealTimeStock()
}
package com.neverdiesoul.domain.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRepository {
    fun getCoinMarketCodeAll(): Flow<Boolean>
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
}
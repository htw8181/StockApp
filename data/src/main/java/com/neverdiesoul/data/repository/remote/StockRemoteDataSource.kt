package com.neverdiesoul.data.repository.remote

import okhttp3.WebSocketListener

interface StockRemoteDataSource {
    fun getCoinMarketCodeAll()
    fun getRealTimeStock(webSocketListener: WebSocketListener)

    fun closeRealTimeStock()
}
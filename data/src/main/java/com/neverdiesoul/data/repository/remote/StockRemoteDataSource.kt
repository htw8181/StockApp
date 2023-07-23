package com.neverdiesoul.data.repository.remote

import okhttp3.WebSocketListener

interface StockRemoteDataSource {
    fun getRealTimeStock(webSocketListener: WebSocketListener)

    fun closeRealTimeStock()
}
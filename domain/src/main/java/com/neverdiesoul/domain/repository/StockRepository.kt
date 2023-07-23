package com.neverdiesoul.domain.repository

import okhttp3.WebSocketListener

interface StockRepository {
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
}
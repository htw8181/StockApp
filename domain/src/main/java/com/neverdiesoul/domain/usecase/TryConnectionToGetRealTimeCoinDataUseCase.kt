package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.repository.StockRepository
import okhttp3.WebSocketListener
import javax.inject.Inject

class TryConnectionToGetRealTimeCoinDataUseCase @Inject constructor(private val repository: StockRepository) {
    private var webSocketListener: WebSocketListener = object : WebSocketListener(){}

    fun setWebSocketListener(webSocketListener: WebSocketListener) {
        this.webSocketListener = webSocketListener
    }

    operator fun invoke() {
        repository.getRealTimeStock(webSocketListener)
    }

    fun closeRealTimeStock() {
        repository.closeRealTimeStock()
    }
}
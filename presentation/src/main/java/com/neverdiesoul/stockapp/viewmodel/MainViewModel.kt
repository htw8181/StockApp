package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.neverdiesoul.domain.usecase.GetRealTimeStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getRealTimeStockUseCase: GetRealTimeStockUseCase) : ViewModel() {
    init {
        getRealTimeStockUseCase.setWebSocketListener(RealTimeStockListener())
    }

    fun getRealTimeStock() {
        getRealTimeStockUseCase()
    }
    private class RealTimeStockListener : WebSocketListener() {
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("웹소켓","수신=> $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }
    }

    override fun onCleared() {
        super.onCleared()

        getRealTimeStockUseCase.closeRealTimeStock()
        Log.d("웹소켓","통신 닫힘")
    }
}
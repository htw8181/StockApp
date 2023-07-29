package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.neverdiesoul.data.repository.remote.websocket.UpbitTicket
import com.neverdiesoul.data.repository.remote.websocket.UpbitType
import com.neverdiesoul.data.repository.remote.websocket.UpbitWebSocketResponseData
import com.neverdiesoul.data.repository.remote.websocket.WebSocketConstants
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllUseCase
import com.neverdiesoul.domain.usecase.GetRealTimeStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.*
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
            Log.d(WebSocketConstants.TAG,"onClosed")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d(WebSocketConstants.TAG,"onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(WebSocketConstants.TAG,"onFailure")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(WebSocketConstants.TAG,"수신 text=> $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d(WebSocketConstants.TAG,"수신 bytes=> ${bytes.toString()}")
            try {
                val res = Gson().fromJson(bytes.string(Charsets.UTF_8),UpbitWebSocketResponseData::class.java)
                Log.d(WebSocketConstants.TAG,"수신 bytes=> $res")
            } catch (e: Exception) {
                Log.d(WebSocketConstants.TAG,e.message.toString())
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            val sendData = Gson().toJson(listOf(UpbitTicket(UUID.randomUUID().toString()), UpbitType("ticker", listOf("KRW-BTC"))))
            Log.d(WebSocketConstants.TAG,"onOpen : $sendData")
            //webSocket.send("[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\"]}]")
            webSocket.send(sendData)
        }
    }

    override fun onCleared() {
        super.onCleared()

        getRealTimeStockUseCase.closeRealTimeStock()
        Log.d(WebSocketConstants.TAG,"통신 닫힘")
    }
}
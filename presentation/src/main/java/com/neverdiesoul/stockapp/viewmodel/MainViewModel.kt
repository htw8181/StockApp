package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.neverdiesoul.data.repository.remote.websocket.UpbitTicket
import com.neverdiesoul.data.repository.remote.websocket.UpbitType
import com.neverdiesoul.data.repository.remote.websocket.UpbitWebSocketResponseData
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.usecase.GetCoinMarketCodeAllFromLocalUseCase
import com.neverdiesoul.domain.usecase.GetRealTimeStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getRealTimeStockUseCase: GetRealTimeStockUseCase, private val getCoinMarketCodeAllFromLocalUseCase: GetCoinMarketCodeAllFromLocalUseCase) : ViewModel() {
    private val tag = this::class.simpleName

    private var _coinMarketCodes: MutableLiveData<List<CoinMarketCode>> = MutableLiveData(mutableListOf())
    val coinMarketCodes: LiveData<List<CoinMarketCode>> = _coinMarketCodes

    init {
        getRealTimeStockUseCase.setWebSocketListener(RealTimeStockListener())
    }

    fun getRealTimeStock() {
        getRealTimeStockUseCase()
    }
    private class RealTimeStockListener : WebSocketListener() {
        private val tag = this::class.simpleName
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d(tag,"onClosed")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d(tag,"onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(tag,"onFailure")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(tag,"수신 text=> $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d(tag,"수신 bytes=> ${bytes.toString()}")
            try {
                val res = Gson().fromJson(bytes.string(Charsets.UTF_8),UpbitWebSocketResponseData::class.java)
                Log.d(tag,"수신 bytes=> $res")
            } catch (e: Exception) {
                Log.d(tag,e.message.toString())
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            val sendData = Gson().toJson(listOf(UpbitTicket(UUID.randomUUID().toString()), UpbitType("ticker", listOf("KRW-BTC"))))
            Log.d(tag,"onOpen : $sendData")
            //webSocket.send("[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\"]}]")
            webSocket.send(sendData)
        }
    }

    override fun onCleared() {
        super.onCleared()

        getRealTimeStockUseCase.closeRealTimeStock()
        Log.d(tag,"RealTimeStock 통신 닫힘")
    }

    fun getCoinMarketCodeAllFromLocal() {
        viewModelScope.launch {
            getCoinMarketCodeAllFromLocalUseCase()
                .onStart { Log.d(tag,"${GetCoinMarketCodeAllFromLocalUseCase::class.simpleName} onStart")  }
                .onCompletion { Log.d(tag,"${GetCoinMarketCodeAllFromLocalUseCase::class.simpleName} onCompletion") }
                .catch { Log.d(tag,"Error!! ${GetCoinMarketCodeAllFromLocalUseCase::class.simpleName} -> $it") }
                .collect {
                    _coinMarketCodes.value = it
                }
        }
    }
}
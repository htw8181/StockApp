package com.neverdiesoul.stockapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.neverdiesoul.domain.usecase.RequestRealTimeCoinDataUseCase
import com.neverdiesoul.domain.usecase.TryConnectionToGetRealTimeCoinDataUseCase
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

abstract class BaseRealTimeViewModel (
    protected val tryConnectionToGetRealTimeCoinDataUseCase: TryConnectionToGetRealTimeCoinDataUseCase,
    protected val requestRealTimeCoinDataUseCase: RequestRealTimeCoinDataUseCase
) : ViewModel() {
    protected val tag = this::class.simpleName

    protected var webSocket: WebSocket? = null

    fun getRealTimeStock() {
        tryConnectionToGetRealTimeCoinDataUseCase.setWebSocketListener(RealTimeStockListener(this))
        tryConnectionToGetRealTimeCoinDataUseCase()
    }

    abstract fun sendRealTimeCoinDataToView(bytes: ByteString)

    interface ViewEvent {
        fun viewOnReady()
        fun viewOnExit()
    }
    private var viewEvent: ViewEvent? = null
    fun setViewEvent(event: ViewEvent) {
        this.viewEvent = event
    }

    fun closeRealTimeStock() {
        tryConnectionToGetRealTimeCoinDataUseCase.closeRealTimeStock()
    }

    fun getMarketCodeToDisplay(marketCode: String?): String {
        return marketCode?.let {
            val seperatorSymbol = "/"
            val results = it.replace("-",seperatorSymbol).split(seperatorSymbol)
            "${results[1]}$seperatorSymbol${results[0]}"
        } ?: ""
    }

    class RealTimeStockListener(val viewModel: BaseRealTimeViewModel) : WebSocketListener() {
        private val tag = this::class.simpleName
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d(tag,"onClosed")
            viewModel.viewEvent?.viewOnExit()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d(tag,"onClosing")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(tag,"onFailure ${response?.code}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(tag,"수신 text=> $text")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d(tag,"수신 bytes=> $bytes")

            viewModel.sendRealTimeCoinDataToView(bytes)
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            viewModel.webSocket = webSocket
            viewModel.viewEvent?.viewOnReady()
        }
    }
}
package com.neverdiesoul.data.repository.remote

import android.util.Log
import com.neverdiesoul.data.repository.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StockRemoteDataSourceImpl @Inject constructor() : StockRemoteDataSource {
    private lateinit var socket: WebSocket
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun getRealTimeStock(webSocketListener: WebSocketListener) {
        Log.d("웹소켓", "통신 시작")
        coroutineScope.launch {
            val okHttpClient = OkHttpClient().newBuilder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build()

            val request = Request.Builder()
                .url(ApiClient.WEB_SOCKET_ENDPOINT)
                .build()

            socket = okHttpClient.newWebSocket(request, webSocketListener)
        }
    }

    override fun closeRealTimeStock() {
        coroutineScope.launch {
            socket.close(ApiClient.WEB_SOCKET_NORMAL_CLOSURE_STATUS,"closeRealTimeStock!!")
        }
    }
}
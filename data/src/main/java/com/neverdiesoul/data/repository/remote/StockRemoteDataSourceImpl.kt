package com.neverdiesoul.data.repository.remote

import android.util.Log
import com.neverdiesoul.data.repository.api.ApiClient
import com.neverdiesoul.data.repository.api.ApiInterface
import com.neverdiesoul.data.repository.api.CoinMarketCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import javax.inject.Inject

class StockRemoteDataSourceImpl @Inject constructor(private val okHttpClient: OkHttpClient, private val retrofit: Retrofit) : StockRemoteDataSource {
    private lateinit var socket: WebSocket
    private val retrofitService by lazy { retrofit.create(ApiInterface::class.java) }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun getCoinMarketCodeAll() {
        coroutineScope.launch {
            try {
                val response = retrofitService.getCoinMarketCodeAll()
                if (response.isSuccessful) {
                    response.body()?.forEach { Log.d("코인 마켓 코드",it.toString()) }
                } else {
                    Log.d("코인 마켓 코드 수신 에러", "${response.code()} : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("코인 마켓 코드 수신 에러", "${e.message}")
            }
        }
    }

    override fun getRealTimeStock(webSocketListener: WebSocketListener) {
        Log.d("웹소켓", "통신 시작")
        coroutineScope.launch {

            val request = Request.Builder()
                .url(ApiClient.WEB_SOCKET_ENDPOINT)
                .build()

            socket = okHttpClient.newWebSocket(request, webSocketListener)
            //okHttpClient.dispatcher.executorService.shutdown()
        }
    }

    override fun closeRealTimeStock() {
        coroutineScope.launch {
            socket.close(ApiClient.WEB_SOCKET_NORMAL_CLOSURE_STATUS,"closeRealTimeStock!!")
        }
    }
}
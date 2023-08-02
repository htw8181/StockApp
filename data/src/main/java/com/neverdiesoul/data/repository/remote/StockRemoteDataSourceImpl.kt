package com.neverdiesoul.data.repository.remote

import android.util.Log
import com.neverdiesoul.data.api.ApiClient
import com.neverdiesoul.data.api.ApiInterface
import com.neverdiesoul.data.model.ResponseCoinMarketCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.Retrofit
import javax.inject.Inject

class StockRemoteDataSourceImpl @Inject constructor(private val okHttpClient: OkHttpClient, private val retrofit: Retrofit) : StockRemoteDataSource {
    private val tag = this::class.simpleName
    private lateinit var socket: WebSocket
    private val retrofitService by lazy { retrofit.create(ApiInterface::class.java) }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun getCoinMarketCodeAllFromRemote(): Flow<List<ResponseCoinMarketCode>> {
        return flow {
            try {
                val response = retrofitService.getCoinMarketCodeAllFromRemote()
                if (response.isSuccessful) {
                    response.body()?.let {
                        //Log.d(TAG,"코인 마켓 코드 ${it.size}개")
                        it.forEach { responseCoinMarketCode ->  Log.d(tag,"코인 마켓 코드 $responseCoinMarketCode") }
                        emit(it)
                    }
                } else {
                    Log.d(tag, "코인 마켓 코드 수신 에러 ${response.code()} : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d(tag, "코인 마켓 코드 수신 에러 ${e.message}")
            }
        }
    }

    override fun getRealTimeStock(webSocketListener: WebSocketListener) {
        Log.d(tag, "RealTimeStock 통신 시작")
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
            socket.cancel()
            socket.close(ApiClient.WEB_SOCKET_NORMAL_CLOSURE_STATUS,"closeRealTimeStock!!")
        }
    }
}
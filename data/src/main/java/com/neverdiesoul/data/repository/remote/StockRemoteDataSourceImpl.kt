package com.neverdiesoul.data.repository.remote

import android.util.Log
import com.google.gson.Gson
import com.neverdiesoul.data.api.ApiClient
import com.neverdiesoul.data.api.ApiInterface
import com.neverdiesoul.data.model.ResponseCoinCandleChartData
import com.neverdiesoul.data.model.ResponseCoinCurrentPrice
import com.neverdiesoul.data.model.ResponseCoinMarketCode
import com.neverdiesoul.data.model.ResponseCoinOrderBookPrice
import com.neverdiesoul.data.repository.remote.websocket.UpbitTicket
import com.neverdiesoul.domain.model.UpbitType
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
import java.util.*
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
            socket.apply {
                cancel()
                close(ApiClient.WEB_SOCKET_NORMAL_CLOSURE_STATUS,"closeRealTimeStock!!")
            }
        }
    }

    override fun requestRealTimeCoinData(upbitTypeList: List<UpbitType>) {
        val params: MutableList<Any> = mutableListOf(UpbitTicket(UUID.randomUUID().toString()))
        upbitTypeList.forEach {
            params.add(it)
        }
        val sendData = Gson().toJson(params)
        Log.d(tag,"sendWebSocket : $sendData")
        //socket.send("[{\"ticket\":\"test\"},{\"type\":\"ticker\",\"codes\":[\"KRW-BTC\"]}]")
        socket.send(sendData)
    }

    override fun getCoinCurrentPriceFromRemote(markets: List<String>): Flow<List<ResponseCoinCurrentPrice>> {
        val funcName = object{}.javaClass.enclosingMethod.name
        return flow {
            try {
                val response = retrofitService.getCoinCurrentPriceFromRemote(markets)
                if (response.isSuccessful) {
                    response.body()?.let { 
                        it.forEach { responseCoinCurrentPrice ->
                            Log.d(tag, "$funcName collect data -> $responseCoinCurrentPrice")
                        }
                        emit(it)
                    }
                } else {
                    Log.d(tag, "코인 현재가 수신 에러 ${response.code()} : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d(tag, "코인 현재가 수신 에러 ${e.message}")
            }
        }
    }

    override fun getCoinOrderBookPriceFromRemote(markets: List<String>): Flow<List<ResponseCoinOrderBookPrice>> {
        val funcName = object{}.javaClass.enclosingMethod.name
        return flow {
            try {
                val response = retrofitService.getCoinOrderBookPriceFromRemote(markets)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.forEach { responseCoinOrderBookPrice ->
                            Log.d(tag, "$funcName collect data -> $responseCoinOrderBookPrice")
                        }
                        emit(it)
                    }
                } else {
                    Log.d(tag, "코인 호가 수신 에러 ${response.code()} : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d(tag, "코인 호가 수신 에러 ${e.message}")
            }
        }
    }

    override fun getCoinCandleChartDataFromRemote(type: String, unit: String, market: String, to: String, count: Int, convertingPriceUnit: String): Flow<List<ResponseCoinCandleChartData>> {
        val funcName = object{}.javaClass.enclosingMethod.name
        return flow {
            try {
                val response = retrofitService.getCoinCandleChartDataFromRemote(type,unit,market,to,count,convertingPriceUnit)
                if (response.isSuccessful) {
                    response.body()?.let {
                        it.forEach { responseCoinCandleChartData ->
                            Log.d(tag, "$funcName collect data -> $responseCoinCandleChartData")
                        }
                        emit(it)
                    }
                } else {
                    Log.d(tag, "코인 캔들 차트 데이터 수신 에러 ${response.code()} : ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d(tag, "코인 캔들 차트 데이터 수신 에러 ${e.message}")
            }
        }
    }
}
package com.neverdiesoul.data.repository

import android.util.Log
import com.neverdiesoul.data.repository.local.StockLocalDataSource
import com.neverdiesoul.data.repository.remote.StockRemoteDataSource
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.WebSocketListener
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockRemoteDataSource: StockRemoteDataSource,
    private val stockLocalDataSource: StockLocalDataSource
    ) : StockRepository {
    val tag = this::class.simpleName
    override fun getCoinMarketCodeAll(): Flow<Boolean> {
        return flow {
            stockRemoteDataSource.getCoinMarketCodeAll()
                .onStart { Log.d(tag,"onStart")  }
                .onCompletion { Log.d(tag,"onCompletion") }
                .catch {
                    Log.d(tag,"Error!! $it")
                    emit(false)
                }
                .collect { responseCoinMarketCodes ->
                    Log.d(tag,"코인 마켓 코드 ${responseCoinMarketCodes.size}개")
                    responseCoinMarketCodes.mapIndexed { index, responseCoinMarketCode ->
                        Log.d(tag,"코인 마켓 코드 $responseCoinMarketCode")
                        responseCoinMarketCode.toDBEntity(index+1)
                    }.let { coinMarketCodeEntities ->
                        try {
                            // 서로 다른 코루틴 스코프 사이에 데이터를 전달하기 위해 채널을 사용함
                            val channels = Channel<Boolean>()
                            CoroutineScope(Dispatchers.IO).launch {
                                val resultCount = stockLocalDataSource.insertCoinMarketCodeAll(coinMarketCodeEntities)
                                Log.d(tag,"코인 마켓 코드 DB 저장 카운트 ${resultCount.size}")
                                Log.d(tag,"코인 마켓 코드 emit")
                                if (resultCount.size.toLong() == coinMarketCodeEntities.size.toLong()) {
                                    coroutineScope {
                                        channels.send(true)
                                    }
                                } else {
                                    coroutineScope {
                                        channels.send(false)
                                    }
                                }
                                channels.close()
                            }
                            channels.consumeEach {
                                emit(it)
                            }
                        } catch (e: Exception) {
                            Log.d(tag,"Collect Error!! ${e.message}")
                            emit(false)
                        }
                    }
                }
        }
    }

    override fun getRealTimeStock(webSocketListener: WebSocketListener) {
        stockRemoteDataSource.getRealTimeStock(webSocketListener)
    }

    override fun closeRealTimeStock() {
        stockRemoteDataSource.closeRealTimeStock()
    }
}
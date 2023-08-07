package com.neverdiesoul.data.repository

import android.util.Log
import com.neverdiesoul.data.mapper.Mapper.toDomainCoinCurrentPrice
import com.neverdiesoul.data.repository.local.StockLocalDataSource
import com.neverdiesoul.data.repository.remote.StockRemoteDataSource
import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.WebSocketListener
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockRemoteDataSource: StockRemoteDataSource,
    private val stockLocalDataSource: StockLocalDataSource
    ) : StockRepository {
    val tag = this::class.simpleName
    override fun getCoinMarketCodeAllFromRemote(): Flow<Boolean> {
        val funcName = object{}.javaClass.enclosingMethod?.name
        return flow {
            stockRemoteDataSource.getCoinMarketCodeAllFromRemote()
                .onStart { Log.d(tag,"$funcName onStart")  }
                .onCompletion { Log.d(tag,"$funcName onCompletion") }
                .catch {
                    Log.d(tag,"$funcName Error!! $it")
                    emit(false)
                }
                .collect { responseCoinMarketCodes ->
                    Log.d(tag,"코인 마켓 코드 ${responseCoinMarketCodes.size}개")
                    responseCoinMarketCodes.mapIndexed { index, responseCoinMarketCode ->
                        Log.d(tag,"코인 마켓 코드 $responseCoinMarketCode")
                        responseCoinMarketCode.toDBEntity(index+1)
                    }.let { coinMarketCodeEntities ->
                        val daoFuncName = "insertCoinMarketCodeAll"
                        try {
                            stockLocalDataSource.insertCoinMarketCodeAll(coinMarketCodeEntities)
                                .onStart { Log.d(tag,"$daoFuncName onStart")  }
                                .onCompletion { Log.d(tag,"$daoFuncName onCompletion") }
                                .catch {
                                    Log.d(tag,"$daoFuncName Flow Error!! $it")
                                    emit(false)
                                }
                                .collect {
                                    if (it.size.toLong() == coinMarketCodeEntities.size.toLong()) {
                                        Log.d(tag,"코인 마켓 코드 DB 저장 카운트 ${it.size}")
                                        Log.d(tag,"코인 마켓 코드 emit")
                                        emit(true)
                                    } else {
                                        emit(false)
                                    }
                                }
                        } catch (e: Exception) {
                            Log.d(tag,"$daoFuncName Collect Error!! ${e.message}")
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

    override fun getCoinMarketCodeAllFromLocal(): Flow<List<CoinMarketCode>> = stockLocalDataSource.getCoinMarketCodeAllFromLocal()

    override fun requestRealTimeCoinData(marketCodes: List<CoinMarketCode>) {
        stockRemoteDataSource.requestRealTimeCoinData(marketCodes)
    }

    override fun getCoinCurrentPriceFromRemote(markets: List<String>): Flow<List<CoinCurrentPrice>> = flow {
        stockRemoteDataSource.getCoinCurrentPriceFromRemote(markets).collect {
            emit(it.toDomainCoinCurrentPrice())
        }
    }
}
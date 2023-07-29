package com.neverdiesoul.data.repository

import com.neverdiesoul.data.repository.remote.StockRemoteDataSource
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(private val stockRemoteDataSource: StockRemoteDataSource) : StockRepository {
    override fun getCoinMarketCodeAll(): Flow<List<CoinMarketCode>> = stockRemoteDataSource.getCoinMarketCodeAll()

    override fun getRealTimeStock(webSocketListener: WebSocketListener) {
        stockRemoteDataSource.getRealTimeStock(webSocketListener)
    }

    override fun closeRealTimeStock() {
        stockRemoteDataSource.closeRealTimeStock()
    }
}
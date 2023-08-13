package com.neverdiesoul.data.repository.remote

import com.neverdiesoul.data.model.ResponseCoinCurrentPrice
import com.neverdiesoul.data.model.ResponseCoinMarketCode
import com.neverdiesoul.data.model.ResponseCoinOrderBookPrice
import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface StockRemoteDataSource {
    fun getCoinMarketCodeAllFromRemote(): Flow<List<ResponseCoinMarketCode>>
    fun getRealTimeStock(webSocketListener: WebSocketListener)
    fun closeRealTimeStock()
    fun requestRealTimeCoinData(dataType: String, marketCodes: List<CoinMarketCode>)
    fun getCoinCurrentPriceFromRemote(markets: List<String>): Flow<List<ResponseCoinCurrentPrice>>
    fun getCoinOrderBookPriceFromRemote(markets: List<String>): Flow<List<ResponseCoinOrderBookPrice>>
}
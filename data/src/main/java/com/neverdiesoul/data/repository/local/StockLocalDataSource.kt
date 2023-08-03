package com.neverdiesoul.data.repository.local

import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity
import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.flow.Flow

interface StockLocalDataSource {
    fun insertCoinMarketCodeAll(list: List<CoinMarketCodeEntity>): Flow<LongArray>

    fun getCoinMarketCodeAllFromLocal(): Flow<List<CoinMarketCode>>
}
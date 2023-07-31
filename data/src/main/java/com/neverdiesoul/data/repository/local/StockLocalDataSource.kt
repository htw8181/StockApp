package com.neverdiesoul.data.repository.local

import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity

interface StockLocalDataSource {
    fun insertCoinMarketCodeAll(list: List<CoinMarketCodeEntity>): LongArray
}
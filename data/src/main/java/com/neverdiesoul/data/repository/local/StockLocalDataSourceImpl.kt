package com.neverdiesoul.data.repository.local

import android.util.Log
import com.neverdiesoul.data.db.dao.StockDao
import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class StockLocalDataSourceImpl @Inject constructor(private val stockDao: StockDao) : StockLocalDataSource {
    private val tag = this::class.simpleName
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun insertCoinMarketCodeAll(list: List<CoinMarketCodeEntity>): LongArray {
        val resultCount = stockDao.insertCoinMarketCodeEntity(list)
        Log.d(tag,"insertCoinMarketCodeEntity 완료")
        return resultCount
    }
}
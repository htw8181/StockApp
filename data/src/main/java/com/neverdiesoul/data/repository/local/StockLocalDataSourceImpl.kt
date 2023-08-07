package com.neverdiesoul.data.repository.local

import com.neverdiesoul.data.db.dao.StockDao
import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity
import com.neverdiesoul.data.mapper.Mapper.toDomainCoinMarketCode
import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StockLocalDataSourceImpl @Inject constructor(private val stockDao: StockDao) : StockLocalDataSource {
    private val tag = this::class.simpleName

    override fun insertCoinMarketCodeAll(list: List<CoinMarketCodeEntity>): Flow<LongArray> = flow {
        emit(stockDao.insertCoinMarketCodeEntity(*list.toTypedArray()))
    }

    override fun getCoinMarketCodeAllFromLocal(): Flow<List<CoinMarketCode>> = flow {
        emit(stockDao.getCoinMarketCodeAllFromLocal().toDomainCoinMarketCode())
    }
}
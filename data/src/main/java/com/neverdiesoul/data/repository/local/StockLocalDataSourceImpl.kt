package com.neverdiesoul.data.repository.local

import android.util.Log
import com.neverdiesoul.data.db.dao.StockDao
import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity
import com.neverdiesoul.data.mapper.Mapper.toDomain
import com.neverdiesoul.domain.model.CoinMarketCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StockLocalDataSourceImpl @Inject constructor(private val stockDao: StockDao) : StockLocalDataSource {
    private val tag = this::class.simpleName
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun insertCoinMarketCodeAll(list: List<CoinMarketCodeEntity>): LongArray {
        val resultCount = stockDao.insertCoinMarketCodeEntity(*list.toTypedArray())
        Log.d(tag,"insertCoinMarketCodeEntity 완료")
        return resultCount
    }

    override fun getCoinMarketCodeAllFromLocal(): Flow<List<CoinMarketCode>> = flow {
        // 서로 다른 코루틴 스코프 사이에 데이터를 전달하기 위해 채널을 사용함
        val channels = Channel<List<CoinMarketCode>>()
        coroutineScope.launch {
            val result = stockDao.getCoinMarketCodeAllFromLocal().toDomain()
            channels.send(result)
            channels.close()
        }
        channels.consumeEach { emit(it) }
    }
}
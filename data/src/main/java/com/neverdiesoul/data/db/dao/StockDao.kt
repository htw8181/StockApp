package com.neverdiesoul.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE/*Insert 할때 PrimaryKey가 겹치는 것이 있으면 덮어 쓴다*/)
    fun insertCoinMarketCodeEntity(list: List<CoinMarketCodeEntity>): LongArray
}
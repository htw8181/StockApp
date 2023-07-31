package com.neverdiesoul.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neverdiesoul.data.db.dao.StockDao
import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity

@Database(entities = [CoinMarketCodeEntity::class], version = 1)
abstract class StockDataBase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}
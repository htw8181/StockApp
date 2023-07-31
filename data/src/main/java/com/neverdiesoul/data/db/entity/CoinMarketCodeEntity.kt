package com.neverdiesoul.data.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CoinMarketCodes")
data class CoinMarketCodeEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "market_code_id")
    val marketCodeId: Int = 0,
    @ColumnInfo(name = "market")
    val market: String,
    @ColumnInfo(name = "korean_name")
    val koreanName: String,
    @ColumnInfo(name = "english_name")
    val englishName: String
)

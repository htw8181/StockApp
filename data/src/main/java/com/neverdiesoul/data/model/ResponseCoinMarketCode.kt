package com.neverdiesoul.data.model

import com.google.gson.annotations.SerializedName
import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity

data class ResponseCoinMarketCode(
    @SerializedName("market")
    val market: String,
    @SerializedName("korean_name")
    val koreanName: String,
    @SerializedName("english_name")
    val englishName: String
) {
    fun toDBEntity(marketCodeId: Int): CoinMarketCodeEntity = CoinMarketCodeEntity(marketCodeId = marketCodeId, market = this.market, koreanName = this.koreanName, englishName = this.englishName)
}

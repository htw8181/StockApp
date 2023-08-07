package com.neverdiesoul.data.mapper

import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity
import com.neverdiesoul.data.model.ResponseCoinCurrentPrice

typealias CoinMarketCodeToDomain = com.neverdiesoul.domain.model.CoinMarketCode
typealias CoinCurrentPriceToDomain = com.neverdiesoul.domain.model.CoinCurrentPrice
object Mapper {
    fun List<CoinMarketCodeEntity>.toDomainCoinMarketCode(): List<CoinMarketCodeToDomain> {
        return this.map {
            CoinMarketCodeToDomain(it.market,it.koreanName,it.englishName)
        }
    }

    fun List<ResponseCoinCurrentPrice>.toDomainCoinCurrentPrice(): List<CoinCurrentPriceToDomain> {
        return this.map {
            CoinCurrentPriceToDomain(
                market = it.market,
                tradeDate = it.tradeDate,
                tradeTime = it.tradeTime,
                tradeDateKst = it.tradeDateKst,
                tradeTimeKst = it.tradeTimeKst,
                tradeTimestamp = it.tradeTimestamp,
                openingPrice = it.openingPrice,
                highPrice = it.highPrice,
                lowPrice = it.lowPrice,
                tradePrice = it.tradePrice,
                prevClosingPrice = it.prevClosingPrice,
                change = it.change,
                changePrice = it.changePrice,
                changeRate = it.changeRate,
                signedChangePrice = it.signedChangePrice,
                signedChangeRate = it.signedChangeRate,
                tradeVolume = it.tradeVolume,
                accTradePrice = it.accTradePrice,
                accTradePrice24h = it.accTradePrice24h,
                accTradeVolume = it.accTradeVolume,
                accTradeVolume24h = it.accTradeVolume24h,
                highest52WeekPrice = it.highest52WeekPrice,
                highest52WeekDate = it.highest52WeekDate,
                lowest52WeekPrice = it.lowest52WeekPrice,
                lowest52WeekDate = it.lowest52WeekDate,
                timestamp = it.timestamp)
        }
    }
}
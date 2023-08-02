package com.neverdiesoul.data.mapper

import com.neverdiesoul.data.db.entity.CoinMarketCodeEntity

typealias CoinMarketCodeToDomain = com.neverdiesoul.domain.model.CoinMarketCode
object Mapper {

    fun List<CoinMarketCodeEntity>.toDomain(): List<CoinMarketCodeToDomain> {
        return this.map {
            CoinMarketCodeToDomain(it.market,it.koreanName,it.englishName)
        }
    }
}
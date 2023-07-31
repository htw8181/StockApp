package com.neverdiesoul.data.mapper

import com.neverdiesoul.data.model.ResponseCoinMarketCode

object Mapper {

    fun List<ResponseCoinMarketCode>.toDomain(): List<com.neverdiesoul.domain.model.CoinMarketCode> {
        return this.map {
            com.neverdiesoul.domain.model.CoinMarketCode(it.market,it.koreanName,it.englishName)
        }
    }
}
package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.repository.StockRepository
import javax.inject.Inject

class RequestRealTimeCoinDataUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(dataType: String, marketCodes: List<CoinMarketCode>) {
        repository.requestRealTimeCoinData(dataType, marketCodes)
    }
}
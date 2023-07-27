package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.repository.StockRepository
import javax.inject.Inject

class GetCoinMarketCodeAllUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke() {
        repository.getCoinMarketCodeAll()
    }

}
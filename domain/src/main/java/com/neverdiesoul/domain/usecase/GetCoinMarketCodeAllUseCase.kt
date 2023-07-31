package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinMarketCodeAllUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(): Flow<Boolean> = repository.getCoinMarketCodeAll()
}
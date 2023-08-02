package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinMarketCodeAllFromLocalUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(): Flow<List<CoinMarketCode>> = repository.getCoinMarketCodeAllFromLocal()
}
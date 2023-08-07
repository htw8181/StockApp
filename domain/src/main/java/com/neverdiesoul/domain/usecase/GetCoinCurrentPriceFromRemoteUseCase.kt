package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.CoinCurrentPrice
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinCurrentPriceFromRemoteUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(markets: List<String>): Flow<List<CoinCurrentPrice>> = repository.getCoinCurrentPriceFromRemote(markets)
}
package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.CoinOrderBookPrice
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinOrderBookPriceFromRemoteUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(markets: List<String>): Flow<List<CoinOrderBookPrice>> = repository.getCoinOrderBookPriceFromRemote(markets)
}
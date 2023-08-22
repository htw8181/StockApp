package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinCandleChartDataFromRemoteUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(type: String, unit: String, market: String, to: String, count: Int, convertingPriceUnit: String): Flow<List<CoinCandleChartData>> = repository.getCoinCandleChartDataFromRemote(type,unit,market,to,count,convertingPriceUnit)
}
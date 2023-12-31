package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.model.UpbitType
import com.neverdiesoul.domain.repository.StockRepository
import javax.inject.Inject

class RequestRealTimeCoinDataUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke(upbitTypeList: List<UpbitType>) {
        repository.requestRealTimeCoinData(upbitTypeList)
    }
}
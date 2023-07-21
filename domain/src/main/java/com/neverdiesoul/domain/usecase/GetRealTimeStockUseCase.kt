package com.neverdiesoul.domain.usecase

import com.neverdiesoul.domain.repository.StockRepository
import javax.inject.Inject

class GetRealTimeStockUseCase @Inject constructor(private val repository: StockRepository) {
    operator fun invoke() {
        repository.getRealTimeStock()
    }
}
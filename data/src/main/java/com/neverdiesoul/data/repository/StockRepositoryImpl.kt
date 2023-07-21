package com.neverdiesoul.data.repository

import com.neverdiesoul.data.repository.remote.StockRemoteDataSource
import com.neverdiesoul.domain.repository.StockRepository
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(private val stockRemoteDataSource: StockRemoteDataSource) : StockRepository {
    override fun getRealTimeStock() {
        stockRemoteDataSource.getRealTimeStock()
    }
}
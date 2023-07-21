package com.neverdiesoul.data.repository.remote

import javax.inject.Inject

class StockRemoteDataSourceImpl @Inject constructor() : StockRemoteDataSource {
    override fun getRealTimeStock() {
        TODO("웹소켓 통신 시작")
    }
}
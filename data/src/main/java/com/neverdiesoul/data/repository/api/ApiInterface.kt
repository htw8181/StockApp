package com.neverdiesoul.data.repository.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("v1/market/all")
    suspend fun getCoinMarketCodeAll(): Response<List<CoinMarketCode>>
}
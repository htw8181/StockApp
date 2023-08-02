package com.neverdiesoul.data.api

import com.neverdiesoul.data.model.ResponseCoinMarketCode
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("v1/market/all")
    suspend fun getCoinMarketCodeAllFromRemote(): Response<List<ResponseCoinMarketCode>>
}
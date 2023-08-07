package com.neverdiesoul.data.api

import com.neverdiesoul.data.model.ResponseCoinCurrentPrice
import com.neverdiesoul.data.model.ResponseCoinMarketCode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v1/market/all")
    suspend fun getCoinMarketCodeAllFromRemote(): Response<List<ResponseCoinMarketCode>>

    @GET("v1/ticker")
    suspend fun getCoinCurrentPriceFromRemote(@Query("markets") markets: List<String>): Response<List<ResponseCoinCurrentPrice>>
}
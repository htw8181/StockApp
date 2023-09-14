package com.neverdiesoul.data.api

import com.neverdiesoul.data.model.ResponseCoinCandleChartData
import com.neverdiesoul.data.model.ResponseCoinCurrentPrice
import com.neverdiesoul.data.model.ResponseCoinMarketCode
import com.neverdiesoul.data.model.ResponseCoinOrderBookPrice
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    /**
     * 마켓 코드 조회
     */
    @GET("v1/market/all")
    suspend fun getCoinMarketCodeAllFromRemote(): Response<List<ResponseCoinMarketCode>>

    /**
     * 현재가 정보 조회
     */
    @GET("v1/ticker")
    suspend fun getCoinCurrentPriceFromRemote(@Query("markets") markets: List<String>): Response<List<ResponseCoinCurrentPrice>>

    /**
     * 호가 정보 조회
     */
    @GET("v1/orderbook")
    suspend fun getCoinOrderBookPriceFromRemote(@Query("markets") markets: List<String>): Response<List<ResponseCoinOrderBookPrice>>

    /**
     * 시세 캔들 조회
     */
    @GET("v1/candles/{type}/{unit}")
    suspend fun getCoinCandleChartDataFromRemote(@Path("type") type: String, @Path("unit") unit: String, @Query("market") market: String, @Query("to") to: String, @Query("count") count: Int, @Query("convertingPriceUnit") convertingPriceUnit: String): Response<List<ResponseCoinCandleChartData>>
}
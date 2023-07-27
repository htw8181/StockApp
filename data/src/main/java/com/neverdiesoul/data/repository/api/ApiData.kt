package com.neverdiesoul.data.repository.api

import com.google.gson.annotations.SerializedName

data class CoinMarketCode(
    @SerializedName("market")
    val market: String,
    @SerializedName("korean_name")
    val korean_name: String,
    @SerializedName("english_name")
    val english_name: String
)

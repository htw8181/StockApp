package com.neverdiesoul.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinMarketCode(
    val market: String,
    val korean_name: String,
    val english_name: String
): Parcelable

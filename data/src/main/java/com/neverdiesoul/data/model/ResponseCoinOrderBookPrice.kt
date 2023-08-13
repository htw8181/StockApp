package com.neverdiesoul.data.model

import com.google.gson.annotations.SerializedName

data class ResponseCoinOrderBookPrice (
    /**
     * 마켓 코드 (ex. KRW-BTC)
     */
    @SerializedName("market"            ) val market           : String?                   = null,
    /**
     * 타임스탬프 호가 생성 시각 (millisecond)
     */
    @SerializedName("timestamp"       ) val timestamp      : Long?                      = null,
    /**
     * 호가 매도 총 잔량
     */
    @SerializedName("total_ask_size"  ) val totalAskSize   : Double?                   = null,
    /**
     * 호가 매수 총 잔량
     */
    @SerializedName("total_bid_size"  ) val totalBidSize   : Double?                   = null,
    /**
     * 호가 리스트
     */
    @SerializedName("orderbook_units" ) val coinOrderBookUnits : List<ResponseCoinOrderBookUnit> = listOf(),
)

data class ResponseCoinOrderBookUnit (
    /**
     * 매도 호가
     */
    @SerializedName("ask_price" ) val askPrice : Double? = null,
    /**
     * 매수 호가
     */
    @SerializedName("bid_price" ) val bidPrice : Double? = null,
    /**
     * 매도 잔량
     */
    @SerializedName("ask_size"  ) val askSize  : Double? = null,
    /**
     * 매수 잔량
     */
    @SerializedName("bid_size"  ) val bidSize  : Double? = null
)

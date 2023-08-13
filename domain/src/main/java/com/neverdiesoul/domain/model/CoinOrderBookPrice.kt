package com.neverdiesoul.domain.model

data class CoinOrderBookPrice (
    /**
     * 마켓 코드 (ex. KRW-BTC)
     */
    val market           : String?                   = null,
    /**
     * 타임스탬프 호가 생성 시각 (millisecond)
     */
    val timestamp      : Long?                      = null,
    /**
     * 호가 매도 총 잔량
     */
    val totalAskSize   : Double?                   = null,
    /**
     * 호가 매수 총 잔량
     */
    val totalBidSize   : Double?                   = null,
    /**
     * 호가 리스트
     */
    val orderbookUnits : List<CoinOrderBookUnit> = listOf(),
)

data class CoinOrderBookUnit (
    /**
     * 매도 호가
     */
    val askPrice : Double? = null,
    /**
     * 매수 호가
     */
    val bidPrice : Double? = null,
    /**
     * 매도 잔량
     */
    val askSize  : Double? = null,
    /**
     * 매수 잔량
     */
    val bidSize  : Double? = null
)

package com.neverdiesoul.stockapp.viewmodel.model

/**
 * 상세 화면 리스트에 보여줄 코인 호가 정보
 */
data class CoinOrderbookUnitForDetailView (
    /**
     * true: 매도, false: 매수
     */
    val isAsk: Boolean = true,
    /**
     * 호가
     */
    val price: Double? = null,
    /**
     * 잔량
     */
    val size: Double? = null
)

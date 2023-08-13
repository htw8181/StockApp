package com.neverdiesoul.stockapp.viewmodel.model

import java.math.BigDecimal

/**
 * 화면에 보여줄 코인 현재가 정보
 */
data class CoinCurrentPriceForView(
    /**
     * 종목 구분 코드
     */
    val market             : String? = null,
    /**
     * 종가(현재가)
     */
    var tradePrice         : Double?    = null,
    /**
     * 변화율의 절대값
     */
    var changeRate         : Double? = null,
    /**
     * EVEN : 보합, RISE : 상승, FALL : 하락
     */
    var change             : String? = null,
    /**
     * 변화액의 절대값
     */
    var changePrice        : BigDecimal?    = null,
    /**
     * 24시간 누적 거래대금
     */
    var accTradePrice24h   : BigDecimal? = null,
    /**
     * 실시간으로 새로 들어온 데이터 인지 여부
     */
    var isNewData          : Boolean? = false
)

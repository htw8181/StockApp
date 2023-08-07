package com.neverdiesoul.domain.model

import java.math.BigDecimal

data class CoinCurrentPrice (
    /**
     * 종목 구분 코드
     */
    val market             : String? = null,
    /**
     * 최근 거래 일자(UTC) 포맷: yyyyMMdd
     */
    val tradeDate          : String? = null,
    /**
     * 최근 거래 시각(UTC) 포맷: HHmmss
     */
    val tradeTime          : String? = null,
    /**
     * 최근 거래 일자(KST) 포맷: yyyyMMdd
     */
    val tradeDateKst       : String? = null,
    /**
     * 최근 거래 시각(KST) 포맷: HHmmss
     */
    val tradeTimeKst       : String? = null,
    /**
     * 최근 거래 일시(UTC) 포맷: Unix Timestamp
     */
    val tradeTimestamp     : Long?    = null,
    /**
     * 시가
     */
    val openingPrice       : Double?    = null,
    /**
     * 고가
     */
    val highPrice          : Double?    = null,
    /**
     * 저가
     */
    val lowPrice           : Double?    = null,
    /**
     * 종가(현재가)
     */
    val tradePrice         : Double?    = null,
    /**
     * 전일 종가(UTC 0시 기준)
     */
    val prevClosingPrice   : Double?    = null,
    /**
     * EVEN : 보합, RISE : 상승, FALL : 하락
     */
    val change             : String? = null,
    /**
     * 변화액의 절대값
     */
    val changePrice        : BigDecimal?    = null,
    /**
     * 변화율의 절대값
     */
    val changeRate         : Double? = null,
    /**
     * 부호가 있는 변화액
     */
    val signedChangePrice  : Double?    = null,
    /**
     * 부호가 있는 변화율
     */
    val signedChangeRate   : Double? = null,
    /**
     * 가장 최근 거래량
     */
    val tradeVolume        : Double? = null,
    /**
     * 누적 거래대금(UTC 0시 기준)
     */
    val accTradePrice      : Double? = null,
    /**
     * 24시간 누적 거래대금
     */
    val accTradePrice24h   : BigDecimal? = null,
    /**
     * 누적 거래량(UTC 0시 기준)
     */
    val accTradeVolume     : Double? = null,
    /**
     * 24시간 누적 거래량
     */
    val accTradeVolume24h  : Double? = null,
    /**
     * 52주 신고가
     */
    val highest52WeekPrice : Double?    = null,
    /**
     * 52주 신고가 달성일 포맷: yyyy-MM-dd
     */
    val highest52WeekDate  : String? = null,
    /**
     * 52주 신저가
     */
    val lowest52WeekPrice  : Double?    = null,
    /**
     * 52주 신저가 달성일 포맷: yyyy-MM-dd
     */
    val lowest52WeekDate   : String? = null,
    /**
     * 타임스탬프
     */
    val timestamp          : Long?    = null
)

package com.neverdiesoul.domain.model

data class CoinCandleChartData(
    /**
     * 마켓명
     */
    var market               : String? = null,
    /**
     * 캔들 기준 시각(UTC 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
     */
    var candleDateTimeUtc    : String? = null,
    /**
     * 캔들 기준 시각(KST 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
     */
    var candleDateTimeKst    : String? = null,
    /**
     * 시가
     */
    var openingPrice         : Double? = null,
    /**
     * 고가
     */
    var highPrice            : Double? = null,
    /**
     * 저가
     */
    var lowPrice             : Double? = null,
    /**
     * 종가
     */
    var tradePrice           : Double? = null,
    /**
     * 마지막 틱이 저장된 시각
     */
    var timestamp            : Long?   = null,
    /**
     * 누적 거래 금액
     */
    var candleAccTradePrice  : Double? = null,
    /**
     * 누적 거래량
     */
    var candleAccTradeVolume : Double? = null,
    /**
     * 분 단위(유닛)
     */
    var unit                 : Int?    = null,
    /**
     * 캔들 기간의 가장 첫 날
     */
    var firstDayOfPeriod  : String? = null,
    /**
     * 전일 종가(UTC 0시 기준)
     */
    var prevClosingPrice     : Double? = null,
    /**
     * 전일 종가 대비 변화 금액
     */
    var changePrice          : Double? = null,
    /**
     * 전일 종가 대비 변화량
     */
    var changeRate           : Double? = null,
    /**
     * 종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)
     */
    var convertedTradePrice  : Double? = null
)

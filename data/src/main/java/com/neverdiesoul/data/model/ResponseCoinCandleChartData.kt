package com.neverdiesoul.data.model

import com.google.gson.annotations.SerializedName


data class ResponseCoinCandleChartData (
    /**
     * 마켓명
     */
    @SerializedName("market"                  ) var market               : String? = null,
    /**
     * 캔들 기준 시각(UTC 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
     */
    @SerializedName("candle_date_time_utc"    ) var candleDateTimeUtc    : String? = null,
    /**
     * 캔들 기준 시각(KST 기준) 포맷: yyyy-MM-dd'T'HH:mm:ss
     */
    @SerializedName("candle_date_time_kst"    ) var candleDateTimeKst    : String? = null,
    /**
     * 시가
     */
    @SerializedName("opening_price"           ) var openingPrice         : Double? = null,
    /**
     * 고가
     */
    @SerializedName("high_price"              ) var highPrice            : Double? = null,
    /**
     * 저가
     */
    @SerializedName("low_price"               ) var lowPrice             : Double? = null,
    /**
     * 종가
     */
    @SerializedName("trade_price"             ) var tradePrice           : Double? = null,
    /**
     * 마지막 틱이 저장된 시각
     */
    @SerializedName("timestamp"               ) var timestamp            : Long?   = null,
    /**
     * 누적 거래 금액
     */
    @SerializedName("candle_acc_trade_price"  ) var candleAccTradePrice  : Double? = null,
    /**
     * 누적 거래량
     */
    @SerializedName("candle_acc_trade_volume" ) var candleAccTradeVolume : Double? = null,
    /**
     * 분 단위(유닛)
     */
    @SerializedName("unit")                     var unit                 : Int?    = null,
    /**
     * 캔들 기간의 가장 첫 날
     */
    @SerializedName("first_day_of_period")      var firstDayOfPeriod  : String? = null,
    /**
     * 전일 종가(UTC 0시 기준)
     */
    @SerializedName("prev_closing_price"      ) var prevClosingPrice     : Double? = null,
    /**
     * 전일 종가 대비 변화 금액
     */
    @SerializedName("change_price"            ) var changePrice          : Double? = null,
    /**
     * 전일 종가 대비 변화량
     */
    @SerializedName("change_rate"             ) var changeRate           : Double? = null,
    /**
     * 종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)
     */
    @SerializedName("converted_trade_price"   ) var convertedTradePrice  : Double? = null
)
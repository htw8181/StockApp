package com.neverdiesoul.data.repository.remote.websocket

import com.google.gson.annotations.SerializedName


data class UpbitTicket(val ticket: String)
data class UpbitType(val type: String, val codes: List<String>)

data class UpbitWebSocketResponseData (
    /**
     * 타입 (ticker - 현재가, trade - 체결, orderbook - 호가, myTrade - 내 체결)
     */
    @SerializedName("type"                  ) var type               : String?  = null,
    /**
     * 마켓 코드 (ex. KRW-BTC)
     */
    @SerializedName("code"                  ) var code               : String?  = null,
    /**
     * 시가
     */
    @SerializedName("opening_price"         ) var openingPrice       : Double?     = null,
    /**
     * 고가
     */
    @SerializedName("high_price"            ) var highPrice          : Double?     = null,
    /**
     * 저가
     */
    @SerializedName("low_price"             ) var lowPrice           : Double?     = null,
    /**
     * 현재가
     */
    @SerializedName("trade_price"           ) var tradePrice         : Double?     = null,
    /**
     * 전일 종가
     */
    @SerializedName("prev_closing_price"    ) var prevClosingPrice   : Double?     = null,
    /**
     * 전일 대비(RISE - 상승, EVEN - 보합, FALL - 하락)
     */
    @SerializedName("change"                ) var change             : String?  = null,
    /**
     * 부호 없는 전일 대비 값
     */
    @SerializedName("change_price"          ) var changePrice        : Double?     = null,
    /**
     * 전일 대비 값
     */
    @SerializedName("signed_change_price"   ) var signedChangePrice  : Double?     = null,
    /**
     * 부호 없는 전일 대비 등락율
     */
    @SerializedName("change_rate"           ) var changeRate         : Double?  = null,
    /**
     * 전일 대비 등락율
     */
    @SerializedName("signed_change_rate"    ) var signedChangeRate   : Double?  = null,
    /**
     * 가장 최근 거래량
     */
    @SerializedName("trade_volume"          ) var tradeVolume        : Double?  = null,
    /**
     * 누적 거래량(UTC 0시 기준)
     */
    @SerializedName("acc_trade_volume"      ) var accTradeVolume     : Double?  = null,
    /**
     * 24시간 누적 거래량
     */
    @SerializedName("acc_trade_volume_24h"  ) var accTradeVolume24h  : Double?  = null,
    /**
     * 누적 거래대금(UTC 0시 기준)
     */
    @SerializedName("acc_trade_price"       ) var accTradePrice      : Double?  = null,
    /**
     * 24시간 누적 거래대금
     */
    @SerializedName("acc_trade_price_24h"   ) var accTradePrice24h   : Double?  = null,
    /**
     * 최근 거래 일자(UTC) (yyyyMMdd)
     */
    @SerializedName("trade_date"            ) var tradeDate          : String?  = null,
    /**
     * 최근 거래 시각(UTC) (HHmmss)
     */
    @SerializedName("trade_time"            ) var tradeTime          : String?  = null,
    /**
     * 체결 타임스탬프 (milliseconds)
     */
    @SerializedName("trade_timestamp"       ) var tradeTimestamp     : Long?     = null,
    /**
     * 매수/매도 구분 (ASK - 매도, BID - 매수)
     */
    @SerializedName("ask_bid"               ) var askBid             : String?  = null,
    /**
     * 누적 매도량
     */
    @SerializedName("acc_ask_volume"        ) var accAskVolume       : Double?  = null,
    /**
     * 누적 매수량
     */
    @SerializedName("acc_bid_volume"        ) var accBidVolume       : Double?  = null,
    /**
     * 52주 최고가
     */
    @SerializedName("highest_52_week_price" ) var highest52WeekPrice : Double?     = null,
    /**
     * 52주 최고가 달성일 (yyyy-MM-dd)
     */
    @SerializedName("highest_52_week_date"  ) var highest52WeekDate  : String?  = null,
    /**
     * 52주 최저가
     */
    @SerializedName("lowest_52_week_price"  ) var lowest52WeekPrice  : Double?     = null,
    /**
     * 52주 최저가 달성일 (yyyy-MM-dd)
     */
    @SerializedName("lowest_52_week_date"   ) var lowest52WeekDate   : String?  = null,
    /**
     * 거래상태(PREVIEW - 입금지원, ACTIVE - 거래지원가능, DELISTED - 거래지원종료)
     */
    @SerializedName("market_state"          ) var marketState        : String?  = null,
    /**
     * 거래 정지 여부
     */
    @SerializedName("is_trading_suspended"  ) var isTradingSuspended : Boolean? = null,
    /**
     * 상장폐지일
     */
    @SerializedName("delisting_date"        ) var delistingDate      : String?  = null,
    /**
     * 유의 종목 여부(NONE - 해당없음, CAUTION - 투자유의)
     */
    @SerializedName("market_warning"        ) var marketWarning      : String?  = null,
    /**
     * 타임스탬프 (millisecond)
     */
    @SerializedName("timestamp"             ) var timestamp          : Long?     = null,
    /**
     * 스트림 타입(SNAPSHOT - 스냅샷, REALTIME - 실시간)
     */
    @SerializedName("stream_type"           ) var streamType         : String?  = null
)
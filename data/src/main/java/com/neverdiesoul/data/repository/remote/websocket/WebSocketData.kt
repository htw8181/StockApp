package com.neverdiesoul.data.repository.remote.websocket

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


object WebSocketConstants {
    const val TAG = "UpbitWebSocket"
}

data class UpbitTicket(val ticket: String)
enum class RealTimeDataType(val type: String) {
    /**
     * 현재가
     */
    TICKER("ticker"),

    /**
     * 호가
     */
    ORDERBOOK("orderbook"),

    /**
     * 체결
     */
    TRADE("trade")
}
data class UpbitType(val type: String, val codes: List<String>)

data class UpbitRealTimeCoinCurrentPrice (
    /**
     * 타입 (ticker - 현재가, trade - 체결, orderbook - 호가, myTrade - 내 체결)
     */
    @SerializedName("type"                  ) val type               : String?  = null,
    /**
     * 마켓 코드 (ex. KRW-BTC)
     */
    @SerializedName("code"                  ) val code               : String?  = null,
    /**
     * 시가
     */
    @SerializedName("opening_price"         ) val openingPrice       : Double?     = null,
    /**
     * 고가
     */
    @SerializedName("high_price"            ) val highPrice          : Double?     = null,
    /**
     * 저가
     */
    @SerializedName("low_price"             ) val lowPrice           : Double?     = null,
    /**
     * 현재가
     */
    @SerializedName("trade_price"           ) val tradePrice         : Double?     = null,
    /**
     * 전일 종가
     */
    @SerializedName("prev_closing_price"    ) val prevClosingPrice   : Double?     = null,
    /**
     * 전일 대비(RISE - 상승, EVEN - 보합, FALL - 하락)
     */
    @SerializedName("change"                ) val change             : String?  = null,
    /**
     * 부호 없는 전일 대비 값
     */
    @SerializedName("change_price"          ) val changePrice        : BigDecimal?     = null,
    /**
     * 전일 대비 값
     */
    @SerializedName("signed_change_price"   ) val signedChangePrice  : Double?     = null,
    /**
     * 부호 없는 전일 대비 등락율
     */
    @SerializedName("change_rate"           ) val changeRate         : Double?  = null,
    /**
     * 전일 대비 등락율
     */
    @SerializedName("signed_change_rate"    ) val signedChangeRate   : Double?  = null,
    /**
     * 가장 최근 거래량
     */
    @SerializedName("trade_volume"          ) val tradeVolume        : Double?  = null,
    /**
     * 누적 거래량(UTC 0시 기준)
     */
    @SerializedName("acc_trade_volume"      ) val accTradeVolume     : Double?  = null,
    /**
     * 24시간 누적 거래량
     */
    @SerializedName("acc_trade_volume_24h"  ) val accTradeVolume24h  : Double?  = null,
    /**
     * 누적 거래대금(UTC 0시 기준)
     */
    @SerializedName("acc_trade_price"       ) val accTradePrice      : Double?  = null,
    /**
     * 24시간 누적 거래대금
     */
    @SerializedName("acc_trade_price_24h"   ) val accTradePrice24h   : BigDecimal?  = null,
    /**
     * 최근 거래 일자(UTC) (yyyyMMdd)
     */
    @SerializedName("trade_date"            ) val tradeDate          : String?  = null,
    /**
     * 최근 거래 시각(UTC) (HHmmss)
     */
    @SerializedName("trade_time"            ) val tradeTime          : String?  = null,
    /**
     * 체결 타임스탬프 (milliseconds)
     */
    @SerializedName("trade_timestamp"       ) val tradeTimestamp     : Long?     = null,
    /**
     * 매수/매도 구분 (ASK - 매도, BID - 매수)
     */
    @SerializedName("ask_bid"               ) val askBid             : String?  = null,
    /**
     * 누적 매도량
     */
    @SerializedName("acc_ask_volume"        ) val accAskVolume       : Double?  = null,
    /**
     * 누적 매수량
     */
    @SerializedName("acc_bid_volume"        ) val accBidVolume       : Double?  = null,
    /**
     * 52주 최고가
     */
    @SerializedName("highest_52_week_price" ) val highest52WeekPrice : Double?     = null,
    /**
     * 52주 최고가 달성일 (yyyy-MM-dd)
     */
    @SerializedName("highest_52_week_date"  ) val highest52WeekDate  : String?  = null,
    /**
     * 52주 최저가
     */
    @SerializedName("lowest_52_week_price"  ) val lowest52WeekPrice  : Double?     = null,
    /**
     * 52주 최저가 달성일 (yyyy-MM-dd)
     */
    @SerializedName("lowest_52_week_date"   ) val lowest52WeekDate   : String?  = null,
    /**
     * 거래상태(PREVIEW - 입금지원, ACTIVE - 거래지원가능, DELISTED - 거래지원종료)
     */
    @SerializedName("market_state"          ) val marketState        : String?  = null,
    /**
     * 거래 정지 여부
     */
    @SerializedName("is_trading_suspended"  ) val isTradingSuspended : Boolean? = null,
    /**
     * 상장폐지일
     */
    @SerializedName("delisting_date"        ) val delistingDate      : String?  = null,
    /**
     * 유의 종목 여부(NONE - 해당없음, CAUTION - 투자유의)
     */
    @SerializedName("market_warning"        ) val marketWarning      : String?  = null,
    /**
     * 타임스탬프 (millisecond)
     */
    @SerializedName("timestamp"             ) val timestamp          : Long?     = null,
    /**
     * 스트림 타입(SNAPSHOT - 스냅샷, REALTIME - 실시간)
     */
    @SerializedName("stream_type"           ) val streamType         : String?  = null
)

data class UpbitRealTimeCoinOrderBookPrice (
    /**
     * 타입 orderbook : 호가
     */
    @SerializedName("type"            ) val type           : String?                   = null,
    /**
     * 마켓 코드 (ex. KRW-BTC)
     */
    @SerializedName("code"            ) val code           : String?                   = null,
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
    @SerializedName("orderbook_units" ) val orderbookUnits : MutableList<OrderbookUnit> = mutableListOf(),
    /**
     * 타임스탬프 (millisecond)
     */
    @SerializedName("timestamp"       ) val timestamp      : Long?                      = null,
    /**
     * 스트림 타입(SNAPSHOT - 스냅샷, REALTIME - 실시간)
     */
    @SerializedName("stream_type"     ) val streamType     : String?                   = null

)

data class OrderbookUnit (
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
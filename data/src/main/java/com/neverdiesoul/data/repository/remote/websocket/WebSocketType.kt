package com.neverdiesoul.data.repository.remote.websocket

import com.google.gson.annotations.SerializedName


data class UpbitTicket(val ticket: String)
data class UpbitType(val type: String, val codes: List<String>)

data class UpbitWebSocketResponseData (
    @SerializedName("type"                  ) var type               : String?  = null,
    @SerializedName("code"                  ) var code               : String?  = null,
    @SerializedName("opening_price"         ) var openingPrice       : Double?     = null,
    @SerializedName("high_price"            ) var highPrice          : Double?     = null,
    @SerializedName("low_price"             ) var lowPrice           : Double?     = null,
    @SerializedName("trade_price"           ) var tradePrice         : Double?     = null,
    @SerializedName("prev_closing_price"    ) var prevClosingPrice   : Double?     = null,
    @SerializedName("acc_trade_price"       ) var accTradePrice      : Double?  = null,
    @SerializedName("change"                ) var change             : String?  = null,
    @SerializedName("change_price"          ) var changePrice        : Double?     = null,
    @SerializedName("signed_change_price"   ) var signedChangePrice  : Double?     = null,
    @SerializedName("change_rate"           ) var changeRate         : Double?  = null,
    @SerializedName("signed_change_rate"    ) var signedChangeRate   : Double?  = null,
    @SerializedName("ask_bid"               ) var askBid             : String?  = null,
    @SerializedName("trade_volume"          ) var tradeVolume        : Double?  = null,
    @SerializedName("acc_trade_volume"      ) var accTradeVolume     : Double?  = null,
    @SerializedName("trade_date"            ) var tradeDate          : String?  = null,
    @SerializedName("trade_time"            ) var tradeTime          : String?  = null,
    @SerializedName("trade_timestamp"       ) var tradeTimestamp     : Long?     = null,
    @SerializedName("acc_ask_volume"        ) var accAskVolume       : Double?  = null,
    @SerializedName("acc_bid_volume"        ) var accBidVolume       : Double?  = null,
    @SerializedName("highest_52_week_price" ) var highest52WeekPrice : Double?     = null,
    @SerializedName("highest_52_week_date"  ) var highest52WeekDate  : String?  = null,
    @SerializedName("lowest_52_week_price"  ) var lowest52WeekPrice  : Double?     = null,
    @SerializedName("lowest_52_week_date"   ) var lowest52WeekDate   : String?  = null,
    @SerializedName("market_state"          ) var marketState        : String?  = null,
    @SerializedName("is_trading_suspended"  ) var isTradingSuspended : Boolean? = null,
    //@SerializedName("delisting_date"        ) var delistingDate      : String?  = null,
    @SerializedName("market_warning"        ) var marketWarning      : String?  = null,
    @SerializedName("timestamp"             ) var timestamp          : Long?     = null,
    @SerializedName("acc_trade_price_24h"   ) var accTradePrice24h   : Double?  = null,
    @SerializedName("acc_trade_volume_24h"  ) var accTradeVolume24h  : Double?  = null,
    @SerializedName("stream_type"           ) var streamType         : String?  = null
)
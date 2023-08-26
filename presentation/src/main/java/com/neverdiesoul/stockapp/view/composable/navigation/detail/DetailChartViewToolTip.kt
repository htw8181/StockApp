package com.neverdiesoul.stockapp.view.composable.navigation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.stockapp.R
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun DetailChartViewToolTip(selectedCoinCandleChartData: CoinCandleChartData) {
    Column(modifier = Modifier
        .width(IntrinsicSize.Min)
        .offset(15.dp, 15.dp)
        .background(Color(51, 51, 51).copy(0.5f))
        .padding(10.dp)) {

        Row {
            Text(text = selectedCoinCandleChartData.candleDateTimeKst?.replace("T"," ") ?: "", style = TextStyle(color = Color.White))
        }
        Row {
            var changeRate = "0.00"
            val textColor = if (selectedCoinCandleChartData.prevClosingPrice != null && selectedCoinCandleChartData.openingPrice != null) {
                val result = selectedCoinCandleChartData.openingPrice!!.minus(selectedCoinCandleChartData.prevClosingPrice!!)
                changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(result / selectedCoinCandleChartData.prevClosingPrice!! * 100).let {
                    if (it == "0") "0.00" else it
                }
                when {
                    result > 0 -> Color.Red
                    result < 0 -> Color.Blue
                    else -> Color.White
                }
            } else Color.White
            Text(text = stringResource(id = R.string.open_price), modifier = Modifier.weight(0.3f,true), style = TextStyle(color = Color.White))
            Text(text = selectedCoinCandleChartData.openingPrice?.let { DecimalFormat("#,###.####").format(it).toString() } ?: "", modifier = Modifier.weight(0.4f,true), style = TextStyle(color = Color.White, textAlign = TextAlign.End))
            Text(text = "${if (textColor == Color.Red) "+" else ""}${changeRate}%", modifier = Modifier.weight(0.3f,true), style = TextStyle(color = textColor, textAlign = TextAlign.End))
        }
        Row {
            var changeRate = "0.00"
            val textColor = if (selectedCoinCandleChartData.prevClosingPrice != null && selectedCoinCandleChartData.highPrice != null) {
                val result = selectedCoinCandleChartData.highPrice!!.minus(selectedCoinCandleChartData.prevClosingPrice!!)
                changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(result / selectedCoinCandleChartData.prevClosingPrice!!  * 100).let {
                    if (it == "0") "0.00" else it
                }
                when {
                    result > 0 -> Color.Red
                    result < 0 -> Color.Blue
                    else -> Color.White
                }
            } else Color.White
            Text(text = stringResource(id = R.string.high_price), modifier = Modifier.weight(0.3f,true), style = TextStyle(color = textColor))
            Text(text = selectedCoinCandleChartData.highPrice?.let { DecimalFormat("#,###.####").format(it).toString() } ?: "", modifier = Modifier.weight(0.4f,true), style = TextStyle(color = textColor, textAlign = TextAlign.End))
            Text(text = "${if (textColor == Color.Red) "+" else ""}${changeRate}%", modifier = Modifier.weight(0.3f,true), style = TextStyle(color = textColor, textAlign = TextAlign.End))
        }
        Row {
            var changeRate = "0.00"
            val textColor = if (selectedCoinCandleChartData.prevClosingPrice != null && selectedCoinCandleChartData.lowPrice != null) {
                val result = selectedCoinCandleChartData.lowPrice!!.minus(selectedCoinCandleChartData.prevClosingPrice!!)
                changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(result / selectedCoinCandleChartData.prevClosingPrice!!  * 100).let {
                    if (it == "0") "0.00" else it
                }
                when {
                    result > 0 -> Color.Red
                    result < 0 -> Color.Blue
                    else -> Color.White
                }
            } else Color.White
            Text(text = stringResource(id = R.string.low_price), modifier = Modifier.weight(0.3f,true), style = TextStyle(color = textColor))
            Text(text = selectedCoinCandleChartData.lowPrice?.let { DecimalFormat("#,###.####").format(it).toString()} ?: "", modifier = Modifier.weight(0.4f,true), style = TextStyle(color = textColor, textAlign = TextAlign.End))
            Text(text = "${if (textColor == Color.Red) "+" else ""}${changeRate}%", modifier = Modifier.weight(0.3f,true), style = TextStyle(color = textColor, textAlign = TextAlign.End))
        }
        Row {
            var changeRate = "0.00"
            val textColor = if (selectedCoinCandleChartData.prevClosingPrice != null && selectedCoinCandleChartData.tradePrice != null) {
                val result = selectedCoinCandleChartData.tradePrice!!.minus(selectedCoinCandleChartData.prevClosingPrice!!)
                changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(result / selectedCoinCandleChartData.prevClosingPrice!!  * 100).let {
                    if (it == "0") "0.00" else it
                }
                when {
                    result > 0 -> Color.Red
                    result < 0 -> Color.Blue
                    else -> Color.White
                }
            } else Color.White
            Text(text = stringResource(id = R.string.close_price), modifier = Modifier.weight(0.3f,true), style = TextStyle(color = Color.White))
            Text(text = selectedCoinCandleChartData.tradePrice?.let { DecimalFormat("#,###.####").format(it).toString()} ?: "", modifier = Modifier.weight(0.4f,true), style = TextStyle(color = Color.White, textAlign = TextAlign.End))
            Text(text = "${if (textColor == Color.Red) "+" else ""}${changeRate}%", modifier = Modifier.weight(0.3f,true), style = TextStyle(color = textColor, textAlign = TextAlign.End))
        }
        Row {
            Text(text = stringResource(id = R.string.trade_amount), modifier = Modifier.weight(0.3f,true), style = TextStyle(color = Color.White))
            val candleAccTradeVolume = DecimalFormat("#,###.###").apply { roundingMode = RoundingMode.HALF_UP }.format(selectedCoinCandleChartData.candleAccTradeVolume).let { result->
                if (result == "0") "0.000" else result
            }
            Text(text = candleAccTradeVolume, modifier = Modifier.weight(0.4f,true), style = TextStyle(color = Color.White, textAlign = TextAlign.End))
            Spacer(modifier = Modifier.weight(0.3f,true))
        }
    }
}
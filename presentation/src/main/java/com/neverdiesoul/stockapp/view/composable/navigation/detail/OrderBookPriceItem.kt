package com.neverdiesoul.stockapp.view.composable.navigation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import com.neverdiesoul.stockapp.viewmodel.model.CoinOrderbookUnitForDetailView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

@Composable
fun OrderBookPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView, coinOrderbookUnitForDetailView: CoinOrderbookUnitForDetailView, onClick: (CoinOrderbookUnitForDetailView)->Unit, isOnlyOrderBookHogaItem: Boolean = false) {
    val isCurrentPrice = coinCurrentPriceForView.tradePrice == coinOrderbookUnitForDetailView.price
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(
            if (coinOrderbookUnitForDetailView.isAsk) Color(
                red = 227,
                green = 235,
                blue = 245
            ) else Color(red = 251, green = 241, blue = 239)
        )
        .border(
            width = if (isCurrentPrice) 1.dp else 0.5.dp,
            color = if (isCurrentPrice) Color.Black else Color.White,
            shape = RectangleShape
        )
        .clickable {
            onClick(coinOrderbookUnitForDetailView)
        }
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End, modifier = Modifier
            //.align(Alignment.CenterVertically)
            .weight(if (isOnlyOrderBookHogaItem) 1.0f else .6f, true)
            .fillMaxHeight()
            .padding(start = 2.dp, end = 2.dp)) {

            OrderBookHogaPriceItem(coinCurrentPriceForView, coinOrderbookUnitForDetailView)
        }

        if (!isOnlyOrderBookHogaItem) {
            Spacer(modifier = Modifier
                .background(color = Color.White)
                .width(1.dp)
                .fillMaxHeight())

            Box(modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(.4f, true)) {

                OrderBookHogaSizeItem(coinOrderbookUnitForDetailView)
            }
        }
    }
}

@Composable
fun OrderBookHogaPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView, coinOrderbookUnitForDetailView: CoinOrderbookUnitForDetailView) {
    /**
     * 변화율 계산 : (호가 - 전일종가) / 전일종가
     */
    var changeRate = 0.0
    val changeRateStr = if ((coinCurrentPriceForView.prevClosingPrice != null && coinOrderbookUnitForDetailView.price != null)) {
        val prevClosingPrice = coinCurrentPriceForView.prevClosingPrice!!
        val price = coinOrderbookUnitForDetailView.price
        changeRate = ((price.minus(prevClosingPrice) ) / prevClosingPrice * 100)
        DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(changeRate).let { result->
            if (result == "0") "0.00" else result
        } ?: "0.00"
    } else "0.00"

    val textColor = when {
        changeRate > 0 -> Color.Red
        changeRate < 0 -> Color.Blue
        else -> Color.Black
    }

    val changeSymbol = when {
        changeRate > 0 -> "+"
        changeRate < 0 -> "-"
        else -> ""
    }

    val orderBookPrice = coinOrderbookUnitForDetailView.price?.let {
        DecimalFormat("#,###").format(it.toInt()).toString()
    } ?: "0"
    Text(color = textColor,text = orderBookPrice)

    Text(color = textColor, text = "$changeSymbol${changeRateStr}%")
}

@Composable
fun OrderBookHogaSizeItem(coinOrderbookUnitForDetailView: CoinOrderbookUnitForDetailView) {
    //잔량
    val size = coinOrderbookUnitForDetailView.size ?: 0.0
    //총잔량
    val totalSize = coinOrderbookUnitForDetailView.totalSize ?: 0.0
    val graphWidth = (( size / totalSize * 1000.0f ).roundToInt() / 1000.0f).run { if (this > 1.0f) 1.0f else this }
    //Log.d("graphWidth","totalSize $totalSize")
    //Log.d("graphWidth","graphWidth $graphWidth")
    Box(modifier = Modifier.fillMaxWidth(graphWidth).fillMaxHeight(.5f).background(if (coinOrderbookUnitForDetailView.isAsk) Color(204,221,242) else Color(245,217,214)))
    Text(textAlign = TextAlign.Start,
        text = coinOrderbookUnitForDetailView.size?.let {
            val result = DecimalFormat("#,###.###").format(it).toString()
            if (result == "0") "0.000" else result
        } ?: "")
}
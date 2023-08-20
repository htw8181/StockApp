package com.neverdiesoul.stockapp.view.composable.navigation.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.neverdiesoul.domain.model.CoinMarketCode
import com.neverdiesoul.stockapp.viewmodel.MainViewModel
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

@Composable
fun MainCurrentPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView, viewModel: MainViewModel, onClick: (CoinMarketCode)->Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .padding(start = 10.dp, end = 10.dp)
        .clickable {
            viewModel.getMarketCodes()?.find { it.market == coinCurrentPriceForView.market}?.let {
                onClick(it)
            }
        }
    )
    {
        val textColor: Color = when(coinCurrentPriceForView.change) {
            "RISE" -> Color.Red
            "FALL" -> Color.Blue
            else -> Color.Black
        }
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.25f, true)) {
            Text(text = coinCurrentPriceForView.market?.let{
                viewModel.getMarketName(it)
            } ?: "")
            Text(text = viewModel.getMarketCodeToDisplay(coinCurrentPriceForView.market))
        }

        Box(modifier = Modifier
            .border(
                width = 2.dp,
                color = if (coinCurrentPriceForView.isNewData == true) textColor else Color.Transparent,
                shape = RectangleShape
            )
            .weight(.25f, true)
            .fillMaxHeight())
        {
            Text(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 2.dp), textAlign = TextAlign.End, color = textColor, text = coinCurrentPriceForView.tradePrice?.let {
                DecimalFormat("#,###.####").format(it).toString()
            } ?: "")

        }
        if (coinCurrentPriceForView.isNewData == true) {
            coinCurrentPriceForView.isNewData = false
        }

        Column(horizontalAlignment = Alignment.End, modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.25f, true)) {
            Text(color = textColor, text = coinCurrentPriceForView.changeRate?.let{
                val changeSymbol = when(coinCurrentPriceForView.change) {
                    "RISE" -> "+"
                    "FALL" -> "-"
                    else -> ""
                }
                val changeRate = DecimalFormat("#.##").apply { roundingMode = RoundingMode.HALF_UP }.format(it * 100).let { result->
                    if (result == "0") "0.00" else result
                }
                "$changeSymbol${changeRate}%"
            } ?: "")
            Text(color = textColor, text = coinCurrentPriceForView.changePrice?.let {
                val changeSymbol = when(coinCurrentPriceForView.change) {
                    "FALL" -> "-"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: "")
        }
        Text(textAlign = TextAlign.End, modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(.25f, true),
            text = coinCurrentPriceForView.accTradePrice24h?.let {
                val result = (it.toDouble() / 100000) * 0.1
                "${DecimalFormat("#,###").format(result.roundToInt()).toString()}백만"
            } ?: "")
    }
}
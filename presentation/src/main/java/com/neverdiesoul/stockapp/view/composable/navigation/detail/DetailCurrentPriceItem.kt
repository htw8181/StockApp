package com.neverdiesoul.stockapp.view.composable.navigation.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun DetailCurrentPriceItem(coinCurrentPriceForView: CoinCurrentPriceForView) {
    val textColor: Color = when(coinCurrentPriceForView.change) {
        "RISE" -> Color.Red
        "FALL" -> Color.Blue
        else -> Color.Black
    }
    Text(color = textColor, text = coinCurrentPriceForView.tradePrice?.let {
        DecimalFormat("#,###.####").format(it).toString()
    } ?: "", fontSize = 25.sp)
    Row {
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
        Text(color = textColor,
            text = coinCurrentPriceForView.changePrice?.let {
                val changeSymbol = when(coinCurrentPriceForView.change) {
                    "RISE" -> "▲"
                    "FALL" -> "▼"
                    else -> ""
                }
                val changePrice = DecimalFormat("#,###.####").apply { roundingMode = RoundingMode.HALF_UP }.format(it).let { result->
                    if (result == "0") "0.0000" else result
                }
                "$changeSymbol$changePrice"
            } ?: "",
            modifier = Modifier.padding(start = 10.dp))
    }
}
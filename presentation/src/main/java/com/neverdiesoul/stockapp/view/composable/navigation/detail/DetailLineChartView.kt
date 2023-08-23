package com.neverdiesoul.stockapp.view.composable.navigation.detail

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.stockapp.databinding.DetailLineChartViewBinding
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import com.neverdiesoul.stockapp.viewmodel.model.CoinCurrentPriceForView

@Composable
fun DetailLineChartView(modifier: Modifier = Modifier, viewModel: DetailViewModel) {
    val funcName = object{}.javaClass.enclosingMethod?.name
    val realTimeCoinCurrentPrice by viewModel.coinCurrentPriceForViewSharedFlow.collectAsState(initial = CoinCurrentPriceForView())
    val coinLineChartDataList: List<CoinCandleChartData> by viewModel.coinLineChartDataList.observeAsState(initial = mutableListOf())
    AndroidViewBinding(factory = DetailLineChartViewBinding::inflate, modifier = Modifier.fillMaxWidth().wrapContentHeight().then(modifier)) {
        Log.d(funcName,"AndroidViewBinding DetailLineChartViewBinding Candle Chart!!")
        if (coinLineChartDataList.isNullOrEmpty()) return@AndroidViewBinding
        val entries = ArrayList<Entry>()
        val entryColors = ArrayList<Int>()
        val average = realTimeCoinCurrentPrice.prevClosingPrice ?: 0.0
        coinLineChartDataList.forEachIndexed { index, coinLineChartData ->
            entries.add(
                Entry(index.toFloat(),coinLineChartData.tradePrice?.toFloat() ?: 0.0f)
            )
            when {
                coinLineChartData.tradePrice != null && coinLineChartData.tradePrice!! > average -> entryColors.add(ChartColor.RED)
                coinLineChartData.tradePrice != null && coinLineChartData.tradePrice!! < average -> entryColors.add(ChartColor.BLUE)
                else -> entryColors.add(ChartColor.LTGRAY)
            }
        }

        chartLine.setViewPortOffsets(0f,0f,0f,0f)

        val dataSet = LineDataSet(entries, "").apply {
            colors = entryColors
            setDrawCircles(false)
            highLightColor = ChartColor.TRANSPARENT
            valueTextSize = 0f
            lineWidth = 1.5f
        }

        val averageLine = LimitLine(average.toFloat()).apply {
            lineWidth = 1f
            enableDashedLine(4f,10f,10f)
            lineColor = ChartColor.DKGRAY
        }
        // 왼쪽 Y 축
        chartLine.axisLeft.run {
            // 라벨, 축라인, 그리드 사용하지 않음
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)

            // 한계선 추가
            removeAllLimitLines()
            addLimitLine(averageLine)
        }

        // 오른쪽 Y 축
        chartLine.axisRight.run {
            isEnabled = false
        }

        // X 축
        chartLine.xAxis.run {
            // x축 값은 투명으로
            textColor = ChartColor.TRANSPARENT
            // 축라인, 그리드 사용하지 않음
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        // 범례
        chartLine.legend.run {
            isEnabled = false
        }

        chartLine.apply {
            data = LineData(dataSet)
            setDrawBorders(false)
            isScaleXEnabled = false
            isScaleYEnabled = false
            description.isEnabled = false
            invalidate()
        }
    }
}
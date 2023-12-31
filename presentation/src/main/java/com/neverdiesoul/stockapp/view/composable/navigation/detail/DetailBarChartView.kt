package com.neverdiesoul.stockapp.view.composable.navigation.detail

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.stockapp.databinding.DetailBarChartViewBinding
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun DetailBarChartView(modifier: Modifier = Modifier, viewModel: DetailViewModel) {
    val funcName = object{}.javaClass.enclosingMethod?.name
    val coinCandleChartDataList: List<CoinCandleChartData> by viewModel.coinCandleChartDataList.observeAsState(initial = mutableListOf())
    var selectedCoinCandleChartData: CoinCandleChartData? by remember {
        mutableStateOf(null)
    }
    var initialized by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        AndroidViewBinding(factory = DetailBarChartViewBinding::inflate, modifier = Modifier.fillMaxSize().then(modifier)) {
            Log.d(funcName,"AndroidViewBinding DetailBarChartViewBinding Candle Chart!!")
            if (coinCandleChartDataList.isNullOrEmpty()) return@AndroidViewBinding
            val entries = ArrayList<BarEntry>()
            val entryColors = ArrayList<Int>()
            coinCandleChartDataList.forEachIndexed { index, coinCandleChartData ->
                entries.add(
                    BarEntry(index.toFloat(),coinCandleChartData.candleAccTradeVolume?.toFloat() ?: 0.0f)
                )
                when {
                    coinCandleChartData.changeRate != null && coinCandleChartData.changeRate!! > 0 -> entryColors.add(ChartColor.RED)
                    coinCandleChartData.changeRate != null && coinCandleChartData.changeRate!! < 0 -> entryColors.add(ChartColor.BLUE)
                    else -> entryColors.add(ChartColor.LTGRAY)
                }
            }

            val dataSet = BarDataSet(entries, "").apply {
                colors = entryColors
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val candleAccTradeVolume = DecimalFormat("#,###.###").apply { roundingMode = RoundingMode.HALF_UP }.format(value).let { result->
                            if (result == "0") "0.000" else result
                        }
                        return  candleAccTradeVolume ?: "0.000"
                    }
                }
                setDrawValues(false)
                // 터치시 노란 선 제거
                highLightColor = ChartColor.TRANSPARENT
            }

            if (!initialized) {
                // 왼쪽 Y 축
                chartBar.axisLeft.run {
                    this.isEnabled = false
                }

                // 오른쪽 Y 축
                chartBar.axisRight.run {
                    isEnabled = true
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val candleAccTradeVolume = DecimalFormat("#,###.###").apply { roundingMode = RoundingMode.HALF_UP }.format(value).let { result->
                                if (result == "0") "0.000" else result
                            }
                            return  candleAccTradeVolume ?: "0.000"
                        }
                    }
                }

                // X 축
                chartBar.xAxis.run {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawAxisLine(true)
                    setDrawGridLines(true)
                    setLabelCount(5,true)
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            val index = value.toInt()
                            if (index >= coinCandleChartDataList.size) return "empty"
                            return coinCandleChartDataList[index].candleDateTimeKst?.substring(0,10) ?: "empty"
                        }
                    }
                }

                // 범례
                chartBar.legend.run {
                    isEnabled = false
                }
                chartBar.apply {
                    data = BarData(dataSet)
                    setDrawBorders(true)
                    isScaleYEnabled = false
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            Log.d(funcName,"$e , $h")
                            h?.let {
                                selectedCoinCandleChartData = coinCandleChartDataList[h.x.toInt()]
                            }
                        }

                        override fun onNothingSelected() {
                            Log.d(funcName,"onNothingSelected")
                            selectedCoinCandleChartData = null
                        }
                    })

                    description.isEnabled = false
                    //isHighlightPerDragEnabled = false
                    //isHighlightPerTapEnabled = false
                    //requestDisallowInterceptTouchEvent(true)
                    zoom(10f,1f, centerOffsets.x * 2 ,0f)
                    invalidate()
                }
                initialized = true
            } else {
                selectedCoinCandleChartData = null
                chartBar.data = BarData(dataSet)
                chartBar.data.notifyDataChanged()

                chartBar.apply {
                    notifyDataSetChanged()
                    invalidate()
                }
            }

            val viewPortHandler = chartBar.viewPortHandler
            Log.d(funcName, "scaleX ${viewPortHandler.scaleX}")
            Log.d(funcName, "scaleY ${viewPortHandler.scaleY}")
        }

        selectedCoinCandleChartData?.let {
            DetailChartViewToolTip(it)
        }
    }

}
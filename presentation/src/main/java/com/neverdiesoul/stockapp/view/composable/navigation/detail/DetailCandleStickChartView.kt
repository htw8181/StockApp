package com.neverdiesoul.stockapp.view.composable.navigation.detail

import android.graphics.Paint
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
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.neverdiesoul.domain.model.CoinCandleChartData
import com.neverdiesoul.stockapp.databinding.DetailCandleStickChartViewBinding
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel

typealias ChartColor = android.graphics.Color

@Composable
fun DetailCandleStickChartView(modifier: Modifier = Modifier, viewModel: DetailViewModel) {
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
        AndroidViewBinding(factory = DetailCandleStickChartViewBinding::inflate, modifier = Modifier
            .fillMaxSize()
            .then(modifier)) {
            Log.d(funcName,"AndroidViewBinding DetailChartTabViewBinding Candle Chart!!")
            if (coinCandleChartDataList.isNullOrEmpty()) return@AndroidViewBinding
            val entries = ArrayList<CandleEntry>()
            coinCandleChartDataList.forEachIndexed { index, coinCandleChartData ->
                entries.add(
                    CandleEntry(index.toFloat(),
                        coinCandleChartData.highPrice?.toFloat() ?: 0.0f,
                        coinCandleChartData.lowPrice?.toFloat() ?: 0.0f,
                        coinCandleChartData.openingPrice?.toFloat()  ?: 0.0f,
                        coinCandleChartData.tradePrice?.toFloat() ?: 0.0f)
                )
            }

            val dataSet = CandleDataSet(entries, "").apply {
                // 심지 부분
                shadowColor = ChartColor.LTGRAY
                shadowWidth = 1F

                // 음봄
                decreasingColor = ChartColor.BLUE
                decreasingPaintStyle = Paint.Style.FILL
                // 양봉
                increasingColor = ChartColor.RED
                increasingPaintStyle = Paint.Style.FILL

                neutralColor = ChartColor.DKGRAY
                setDrawValues(false)
                // 터치시 노란 선 제거
                highLightColor = ChartColor.TRANSPARENT
            }

            if (!initialized) {
                // 왼쪽 Y 축
                chartCandleStick.axisLeft.run {
                    this.isEnabled = false
                }

                // 오른쪽 Y 축
                chartCandleStick.axisRight.run {
                    isEnabled = true
                }

                // X 축
                chartCandleStick.xAxis.run {
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
                chartCandleStick.legend.run {
                    isEnabled = false
                }
                chartCandleStick.apply {
                    data = CandleData(dataSet)
                    setDrawBorders(true)
                    isScaleYEnabled = false
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            Log.d(funcName,"$e , $h")
                            h?.let {
                                selectedCoinCandleChartData = coinCandleChartDataList[h.x.toInt()]
                                dataSet.highLightColor = ChartColor.rgb(255, 187, 115)
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
                chartCandleStick.data = CandleData(dataSet)
                chartCandleStick.data.notifyDataChanged()

                chartCandleStick.apply {
                    notifyDataSetChanged()
                    invalidate()
                }
            }
        }

        selectedCoinCandleChartData?.let {
            DetailChartViewToolTip(it)
        }
    }
}
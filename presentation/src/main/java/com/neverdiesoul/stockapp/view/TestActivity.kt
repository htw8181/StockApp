package com.neverdiesoul.stockapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.neverdiesoul.stockapp.view.composable.navigation.detail.OrderBuyTabContent
import com.neverdiesoul.stockapp.view.ui.theme.StockAppTheme

/**
 * viewModel 이 동반된 composable 로는 preview가 불가능하기 때문에 추후, 개발한 Composable UI 확인은 이곳에서 한다.
 */
class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}

@Composable
private fun MainScreen(modifier: Modifier = Modifier) {
    OrderBuyTabContent(Modifier.fillMaxSize())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TestActivityPreview() {
    StockAppTheme {
        MainScreen()
    }
}
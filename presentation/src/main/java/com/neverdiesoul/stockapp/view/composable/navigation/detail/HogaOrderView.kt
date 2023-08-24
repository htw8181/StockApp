package com.neverdiesoul.stockapp.view.composable.navigation.detail

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.neverdiesoul.stockapp.databinding.DetailHogaTabViewBinding
import com.neverdiesoul.stockapp.viewmodel.DetailViewModel

@Composable
fun HogaOrderView(modifier: Modifier = Modifier, viewModel: DetailViewModel) {
    val funcName = object{}.javaClass.enclosingMethod?.name
    var backEnabled by remember {
        mutableStateOf(false)
    }
    var webView: WebView? by remember {
        mutableStateOf(null)
    }
    AndroidViewBinding(factory = DetailHogaTabViewBinding::inflate, modifier = Modifier.fillMaxSize().then(modifier)) {
        Log.d(funcName ,"AndroidViewBinding DetailHogaTabFragmentBinding")
        if (webView == null) {
            Log.d("onPageStarted" ,"AndroidViewBinding Update")
            webviewDetailHoga.apply {
                webViewClient = object : WebViewClient() {

                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest?
                    ): Boolean {
                        Log.d("onPageStarted","shouldOverrideUrlLoading ${view.url}")
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageStarted(
                        view: WebView,
                        url: String?,
                        favicon: Bitmap?
                    ) {
                        progressBar.visibility = View.VISIBLE
                        backEnabled = view.canGoBack()
                        Log.d("onPageStarted" ,"onPageStarted backEnable $backEnabled")
                        super.onPageStarted(view, url, favicon)
                    }

                    override fun onPageFinished(view: WebView, url: String?) {
                        Log.d("onPageStarted" ,"onPageFinished")
                        progressBar.visibility = View.GONE
                        super.onPageFinished(view, url)
                    }
                }
                webChromeClient = WebChromeClient()
                val webViewSetting = this.settings
                webViewSetting.apply {
                    javaScriptEnabled = true; //웹뷰에서 javascript를 사용하도록 설정
                    //javaScriptCanOpenWindowsAutomatically = false; //멀티윈도우 띄우는 것
                    //loadWithOverviewMode = true; // 메타태그
                    //useWideViewPort = true; //화면 사이즈 맞추기
                    //setSupportZoom(true); // 화면 줌 사용 여부
                    //builtInZoomControls = true; //화면 확대 축소 사용 여부
                    //displayZoomControls = true; //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
                    //cacheMode = WebSettings.LOAD_NO_CACHE; // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
                    //defaultFixedFontSize = 14; //기본 고정 글꼴 크기, value : 1~72 사이의 숫자
                }
                //addJavascriptInterface(Bridge(),"StockAppWebViewBridge")
                loadUrl("https://htw8181.github.io/")
                webView = this
                backEnabled = true
            }
        }

    }
    // 뒤로가기 이벤트가 감지 됐을 때 enabled이 true면 onBack()을 실행하게 된다.
    BackHandler(enabled = backEnabled, onBack = {
        webView?.goBack()
    })
}
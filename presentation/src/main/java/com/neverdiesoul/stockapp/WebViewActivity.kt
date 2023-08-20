package com.neverdiesoul.stockapp

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.neverdiesoul.stockapp.databinding.DetailHogaTabViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var viewBinding: DetailHogaTabViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DetailHogaTabViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.webviewDetailHoga.apply {
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
                    var backEnabled = view.canGoBack()
                    Log.d("onPageStarted" ,"onPageStarted backEnable $backEnabled")
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView, url: String?) {
                    var backEnabled = view.canGoBack()
                    Log.d("onPageStarted" ,"onPageFinished backEnable $backEnabled")
                    super.onPageFinished(view, url)
                }
            }
            webChromeClient = WebChromeClient()
            val webViewSetting = this.settings
            webViewSetting.apply {
                javaScriptEnabled = true; //웹뷰에서 javascript를 사용하도록 설정
                javaScriptCanOpenWindowsAutomatically = false; //멀티윈도우 띄우는 것
                loadWithOverviewMode = true; // 메타태그
                useWideViewPort = true; //화면 사이즈 맞추기
                setSupportZoom(true); // 화면 줌 사용 여부
                builtInZoomControls = true; //화면 확대 축소 사용 여부
                displayZoomControls = true; //화면 확대 축소시, webview에서 확대/축소 컨트롤 표시 여부
                cacheMode = WebSettings.LOAD_NO_CACHE; // 브라우저 캐시 사용 재정의 value : LOAD_DEFAULT, LOAD_NORMAL, LOAD_CACHE_ELSE_NETWORK, LOAD_NO_CACHE, or LOAD_CACHE_ONLY
                defaultFixedFontSize = 14; //기본 고정 글꼴 크기, value : 1~72 사이의 숫자
            }
            //addJavascriptInterface(Bridge(),"StockAppWebViewBridge")
            clearCache(true)
            clearHistory()
            loadUrl("https://poiemaweb.com/")
            /*webView = this
            backEnabled = true*/
        }
    }

    override fun onBackPressed() { // Activity 기준
        if(viewBinding.webviewDetailHoga.canGoBack()) {
            viewBinding.webviewDetailHoga.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
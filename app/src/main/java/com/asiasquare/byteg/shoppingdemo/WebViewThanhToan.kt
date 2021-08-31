package com.asiasquare.byteg.shoppingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.asiasquare.byteg.shoppingdemo.databinding.ActivityWebViewThanhToanBinding


class WebViewThanhToan : AppCompatActivity() {
    private lateinit var binding : ActivityWebViewThanhToanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewThanhToanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.javaScriptEnabled = true
        val url = getPdfUrl()
        binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }
    fun getPdfUrl(): String {
        return "http://www.germanyshoppingsquare.com/server/testfile/paypalthanhtoan.pdf"
    }
}
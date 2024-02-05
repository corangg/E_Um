package com.example.appointment.ui.activity.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.appointment.R
import com.example.appointment.databinding.ActivityAdressBinding
import com.example.appointment.ui.activity.BaseActivity

class AdressSearchActivity : BaseActivity<ActivityAdressBinding>() {
    inner class KaKaoJavaScriptInterface {

        @JavascriptInterface
        fun processDATA(address: String?) {
            Intent().apply {
                putExtra("address", address)
                setResult(RESULT_OK, this)
            }
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")

    override fun layoutResId(): Int {
        return R.layout.activity_adress
    }

    override fun initializeUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(KaKaoJavaScriptInterface(), "Android")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                binding.webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }
        binding.webView.loadUrl("http://project231205-32867.web.app")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun setObserve() {}




}
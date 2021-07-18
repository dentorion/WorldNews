package com.example.worldnews.presentation.ui.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.worldnews.R
import com.example.worldnews.databinding.FragmentArticleWebViewBinding
import com.example.worldnews.presentation.extension.visible
import kotlinx.android.synthetic.main.fragment_article_web_view.*

class ArticleWebView : Fragment(R.layout.fragment_article_web_view) {

    private val binding: FragmentArticleWebViewBinding by viewBinding()
    private val args: ArticleWebViewArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openWebView(args.url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebView(url: String) {
        binding.progressBar.max = 100

        val webView: WebView = binding.webview

        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webview.apply {
            removeAllViews()
            clearCache(true)
            destroy()
        }
    }

}
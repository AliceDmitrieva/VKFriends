package com.alisadmitrieva.vkfriends.ui.login

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.alisadmitrieva.vkfriends.R
import com.alisadmitrieva.vkfriends.ui.friends.FriendsActivity
import com.alisadmitrieva.vkfriends.utils.Config
import com.alisadmitrieva.vkfriends.utils.parseRedirectUrl
import com.alisadmitrieva.vkfriends.utils.saveToken
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

class LoginActivity : AppCompatActivity() {

    private lateinit var progress: MaterialProgressBar
    private lateinit var webView: WebView
    private var vkWebViewClient: VkWebViewClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        webView = findViewById(R.id.web)
        progress = findViewById(R.id.loginProgressbar)

        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = true
        webView.clearCache(true)
        vkWebViewClient = VkWebViewClient()
        webView.webViewClient = vkWebViewClient
        val builder = Uri.Builder()
        builder.scheme("https")
            .authority("oauth.vk.com")
            .appendPath("authorize")
            .appendQueryParameter("client_id", Config.CLIENT_ID)
            .appendQueryParameter("redirect_uri", Config.REDIRECT_URI)
            .appendQueryParameter("display", Config.DISPLAY)
            .appendQueryParameter("response_type", Config.RESPONSE_TYPE)
            .appendQueryParameter("scope", Config.SCOPE)
            .appendQueryParameter("v", Config.VERSION)
        webView.loadUrl(builder.toString())
        webView.visibility = View.VISIBLE

    }

    internal inner class VkWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress.visibility = View.VISIBLE
            parseUrl(url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if (url != null && (url.startsWith("https://oauth.vk.com/authorize")
                        || url.startsWith("https://oauth.vk.com/oauth/authorize"))
            ) {
                progress.visibility = View.GONE
            }
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return false
        }
    }

    private fun parseUrl(url: String?) {
        try {
            if (url == null) {
                return
            }
            if (url.startsWith(Config.REDIRECT_URI)) {
                if (!url.contains("error")) {
                    val auth = parseRedirectUrl(url)
                    webView.visibility = View.GONE
                    progress.visibility = View.VISIBLE

                    saveToken(auth[0])
                    startActivity(Intent(this, FriendsActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, Config.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
                }
            } else if (url.contains("error?err")) {
                Toast.makeText(this, Config.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, Config.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
        }
    }

}

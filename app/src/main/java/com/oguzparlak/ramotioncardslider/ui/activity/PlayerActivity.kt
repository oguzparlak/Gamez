package com.oguzparlak.ramotioncardslider.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.oguzparlak.ramotioncardslider.R
import kotlinx.android.synthetic.main.activity_player.*
import org.json.JSONObject


/**
 * This PlayerActivity will pop as a BottomSheet
 * from the below of the window. It contains a WebView
 * to show the stream to user.
 */
class PlayerActivity : AppCompatActivity() {

    private lateinit var mChannelId: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // Get channelId from previous Activity or Fragment
        mChannelId = intent.getStringExtra("channel_extra")

        // WebView Config
        mPlayerWebView.settings.javaScriptEnabled = true
        mPlayerWebView.addJavascriptInterface(WebAppInterface(), "AndroidBridge")
        mPlayerWebView.webViewClient = TwitchWebViewClient()
        mPlayerWebView.loadUrl("file:///android_res/raw/twitch_player.html")
    }

    inner class TwitchWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            view!!.loadUrl("javascript:init();")
        }
    }

    inner class WebAppInterface {

        /**
         * Returns a JSONObject indication the
         * configurations of a TwitchStream
         */
        @JavascriptInterface
        fun getTwitchConfig(): String {
            return JSONObject().put("channel", mChannelId)
                    .put("width", "100%")
                    .put("height", "100%")
                    .put("chat", "mobile")
                    .put("allowFullScreen", true)
                    .put("layout", "video")
                    .put("theme", "light")
                    .put("autoplay", true).toString()
        }
    }
}

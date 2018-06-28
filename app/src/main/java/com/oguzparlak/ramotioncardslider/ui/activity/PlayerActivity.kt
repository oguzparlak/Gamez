package com.oguzparlak.ramotioncardslider.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.oguzparlak.ramotioncardslider.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mPlayerWebView.settings.javaScriptEnabled = true
        mPlayerWebView.loadUrl("file:///android_res/raw/twitch_player.html")
    }
}

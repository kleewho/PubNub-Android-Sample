package com.example.pubnubsample

import android.app.Activity
import android.os.Bundle

class Conv: Activity() {

    private val channel: String by lazy { "meta.${(1..1000).random()}" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conv)
        MainActivity.pubnub.subscribe(channels = listOf(channel))
    }

    override fun onPause() {
        super.onPause()
        MainActivity.pubnub.unsubscribe(channels = listOf(channel))
    }
}
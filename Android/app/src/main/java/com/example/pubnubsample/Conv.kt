package com.example.pubnubsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_conv.*
import kotlinx.android.synthetic.main.activity_main.*

class Conv: Activity() {

    private val channel: String by lazy { "meta.${(1..1000).random()}" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conv)
        MainActivity.pubnub.subscribe(channels = listOf(channel))
        button.setOnClickListener {
            onClick()
        }
    }

    private fun onClick() {
        val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        MainActivity.pubnub.unsubscribe(channels = listOf(channel))
    }
}
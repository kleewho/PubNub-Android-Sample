package com.example.pubnubsample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_conv.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class Conv : Activity() {

    @Inject
    lateinit var pubNubHolder: PubNubHolder
    private val channel: String by lazy { "meta.${(1..1000).random()}" }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conv)
        pubNubHolder.pubnub.subscribe(channels = listOf(channel))
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
        pubNubHolder.pubnub.unsubscribe(channels = listOf(channel))
    }
}
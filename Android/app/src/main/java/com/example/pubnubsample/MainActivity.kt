package com.example.pubnubsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.enums.PNReconnectionPolicy
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var _pubnub: PubNub? = null
    private var currentMetaChannel: String? = null
    private var listener: SubscribeCallback? = null
    lateinit var okhttp: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pubnubSetup()

        start_test.setOnClickListener {
            subscribeToRandomMeta()
        }

        stop_test.setOnClickListener {
            unsubscribeFromMeta()
        }
    }

    private fun pubnubSetup() {
//        okhttp = OkHttpClient.Builder()
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .retryOnConnectionFailure(true)
//            .addInterceptor(HttpLoggingInterceptor())
//            .build()

        val config = PNConfiguration()
        config.subscribeKey = ""
        config.authKey = ""
        config.uuid = ""
        //config.secure = true
        config.reconnectionPolicy = PNReconnectionPolicy.NONE
        config.logVerbosity = PNLogVerbosity.BODY
        //config.cipherKey = ""
        //config.maximumConnections = 2
        //config.heartbeatInterval = 20
        //config.presenceTimeout = 20

        _pubnub = PubNub(config)

        listener = object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                Log.e("PubNub", pnStatus.toString())

                last_status.text = pnStatus.toString()
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                Log.e("PubNub", pnMessageResult.toString())
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                Log.e("PubNub", pnSignalResult.toString())
            }

        }

        _pubnub!!.addListener(listener!!)

        val group = "cg_1_41033"
        _pubnub?.subscribe(channelGroups = listOf(group))
    }

    private fun subscribeToRandomMeta() {
        val rand = "meta.${(1..1000).random()}"
        currentMetaChannel = rand

        _pubnub?.subscribe(channels = listOf(rand))

//        val i = Intent(this, TestActivity::class.java)
//        startActivityForResult(i, 199)
    }

    private fun unsubscribeFromMeta() {
        currentMetaChannel?.let {
            _pubnub?.unsubscribe(channels = listOf(it))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        unsubscribeFromMeta()
    }
}

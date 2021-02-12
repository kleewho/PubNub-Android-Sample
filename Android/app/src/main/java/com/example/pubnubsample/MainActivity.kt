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
import okhttp3.logging.HttpLoggingInterceptor

class MainActivity : AppCompatActivity() {

    companion object {
        val pubnub: PubNub by lazy { pubnubSetup() }
        private fun pubnubSetup(): PubNub {
            val config = PNConfiguration()
            config.subscribeKey = "sub-c-ec88e64e-b248-11ea-af7b-9a67fd50bac3"
            config.authKey = ""
            config.uuid = "oim8"
            //config.secure = true
            config.reconnectionPolicy = PNReconnectionPolicy.NONE
            config.logVerbosity = PNLogVerbosity.BODY

            //config.cipherKey = ""
            config.maximumConnections = 2
//        config.heartbeatInterval = 20
//        config.presenceTimeout = 20

            return PubNub(config).also { Log.e("PubNub", "Created pubnub $it") }
        }

    }
    private var listener: SubscribeCallback? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
        Log.e("PubNub", "Registered listener $listener")

        pubnub.addListener(listener!!)

        val group = "cg_1_41033"
        pubnub.subscribe(channelGroups = listOf(group))

        start_test.setOnClickListener {
            subscribeToRandomMeta()
        }
    }


    private fun subscribeToRandomMeta() {
        val intent = Intent(this, Conv::class.java)
        startActivity(intent)
    }

}

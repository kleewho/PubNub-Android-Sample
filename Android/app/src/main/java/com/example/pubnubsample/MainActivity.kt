package com.example.pubnubsample

import android.app.Application
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
import dagger.Component
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import javax.inject.Singleton

@Component
@Singleton
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(conv: Conv)
}


// appComponent lives in the Application class to share its lifecycle
class MyApplication : Application() {
    // Reference to the application graph that is used across the whole app
    val appComponent = DaggerApplicationComponent.create()
}

@Singleton
class PubNubHolder @Inject constructor() {
    val pubnub: PubNub by lazy { pubnubSetup() }
    val listener: SubscribeCallback = object : SubscribeCallback() {
        override fun status(pubnub: PubNub, pnStatus: PNStatus) {
            //Thread.sleep(1_000)
            Log.e("PubNub", pnStatus.toString())
            Log.e("PubNub", "Thread: ${Thread.currentThread().id.toString()}")
            //last_status.text = pnStatus.toString()
        }

        override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
            Log.e("PubNub", pnMessageResult.toString())
        }

        override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
            Log.e("PubNub", pnSignalResult.toString())
        }

    }

    private fun pubnubSetup(): PubNub {
        val config = PNConfiguration()
        config.subscribeKey = "sub-c-ec88e64e-b248-11ea-af7b-9a67fd50bac3"
        config.authKey = ""
        config.uuid = "oim8"
        //config.secure = true
        config.reconnectionPolicy = PNReconnectionPolicy.NONE
        config.logVerbosity = PNLogVerbosity.BODY

        //config.cipherKey = ""
        config.maximumConnections = 1
//        config.heartbeatInterval = 20
//        config.presenceTimeout = 20
        Log.e("PubNub", "Registered listener $listener")
        val pubnub = PubNub(config).also { Log.e("PubNub", "Created pubnub $it") }
        pubnub.addListener(listener)

        return pubnub
    }

}

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var pubNubHolder: PubNubHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val group = "cg_1_41033"
        pubNubHolder.pubnub.subscribe(channelGroups = listOf(group))

        start_test.setOnClickListener {
            subscribeToRandomMeta()
        }
    }

    private fun subscribeToRandomMeta() {
        val intent = Intent(this, Conv::class.java)
        startActivity(intent)
    }
}

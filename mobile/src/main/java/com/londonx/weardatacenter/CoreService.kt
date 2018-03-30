package com.londonx.weardatacenter

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.londonx.weardata.ACTION_DATA_RECEIVED
import com.londonx.weardata.EXTRA_DATA
import org.jetbrains.anko.startService
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by London on 2018/3/7.
 * core service
 */
class CoreService : Service(), MessageClient.OnMessageReceivedListener {
    override fun onMessageReceived(p0: MessageEvent) {
        if (p0.data?.isNotEmpty() != true) {
            return
        }
        onDataListener.invoke(p0.data)
        val s = String(p0.data)
        try {
            JSONObject(s)
        } catch (ignore: JSONException) {
            return
        }
        sendBroadcast(Intent(ACTION_DATA_RECEIVED)
                .putExtra(EXTRA_DATA, s))
    }

    override fun onBind(intent: Intent?) = Binder()

    companion object {
        private var started: CoreService.() -> Unit = {}
        var instance: CoreService? = null

        fun start(context: Context, started: CoreService.() -> Unit) {
            this.started = started
            instance?.also {
                this.started.invoke(it)
                return
            }
            context.startService<CoreService>()
        }
    }

    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private var onDataListener: (ByteArray) -> Unit = {}

    override fun onCreate() {
        super.onCreate()

        messageClient.addListener(this)

        instance = this
        started.invoke(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        messageClient.removeListener(this)
    }

    fun setOnDataListener(f: (ByteArray) -> Unit) {
        onDataListener = f
    }
}
package com.londonx.weardatacenter

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.support.wearable.input.RotaryEncoder
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.londonx.weardata.ACTION_CLICK
import com.londonx.weardata.ACTION_CROWN_SCROLL
import com.londonx.weardata.FMT_CMD
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {
    private var node: Node? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvClicker.setOnClickListener {
            sendData(String.format(FMT_CMD, ACTION_CLICK, 0f).toByteArray())
        }
    }

    override fun onResume() {
        super.onResume()
        pbConnecting.visibility = View.VISIBLE
        Wearable.getCapabilityClient(this)
                .getCapability("data_center_service", CapabilityClient.FILTER_ALL)
                .addOnSuccessListener(this) {
                    pbConnecting.visibility = View.INVISIBLE
                    node = it.nodes.firstOrNull()
                    if (node == null) {
                        tvAppNotInstalled.visibility = View.VISIBLE
                        tvClicker.visibility = View.INVISIBLE
                    } else {
                        tvAppNotInstalled.visibility = View.INVISIBLE
                        tvClicker.visibility = View.VISIBLE
                    }
                }
    }

    private var lastEventAt = 0L
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_SCROLL) {
            return super.onGenericMotionEvent(event)
        }
        if (System.currentTimeMillis() - lastEventAt < 27) {//about 36 fps
            lastEventAt = System.currentTimeMillis()
            return false
        }
        val delta = RotaryEncoder.getScaledScrollFactor(this) *
                RotaryEncoder.getRotaryAxisValue(event)
        tvCrown.text = delta.toString()
        sendData(String.format(FMT_CMD, ACTION_CROWN_SCROLL, delta).toByteArray())
        return false
    }


    private var messageId = 0x0L
    private fun sendData(data: ByteArray) {
        node ?: return
        Wearable.getMessageClient(this).sendMessage(node!!.id, "no_path", data)
        messageId++
    }
}

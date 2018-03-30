package com.londonx.weardatacenter

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.textColor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CoreService.start(this) {
            tvServerStatus.text = "SERVER STATUS: OK"
            tvServerStatus.textColor = Color.GREEN
            this.setOnDataListener {
                tvHistory.text = kotlin.text.String(it)
            }
        }
    }
}

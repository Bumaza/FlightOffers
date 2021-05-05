package com.kiwi.task.ui

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kiwi.task.R

open class BaseActivity : AppCompatActivity() {

    var refreshWifiState: Button? = null

    fun initNetworkErrorLayout(){
        refreshWifiState = findViewById(R.id.refresh_network_state)
        refreshWifiState?.setOnClickListener {
            onRefreshNetwork()
        }
    }

    open fun onRefreshNetwork(){

    }
}
package com.kiwi.task.ui

import android.content.pm.PackageManager
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kiwi.task.R
import com.kiwi.task.utils.PermissionKit

open class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }

    var refreshWifiState: Button? = null

    fun initNetworkErrorLayout(){
        refreshWifiState = findViewById(R.id.refresh_network_state)
        refreshWifiState?.setOnClickListener {
            onRefreshNetwork()
        }
    }

    private val onRequestPermissionsResult = { requestCode: Int, permissions: Array<String>,
                                               grantResults: IntArray   ->
        when (requestCode) {
            PermissionKit.REQUEST_CODE_ASK_PERMISSIONS -> {
                for (index in permissions.indices.reversed()) {
                    if(grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        //exit the app if one permission is not granted
                        //finish()
                    }
                }
            }
        }
    }

    open fun onPermissionsGranted(){

    }

    open fun onRefreshNetwork(){

    }
}
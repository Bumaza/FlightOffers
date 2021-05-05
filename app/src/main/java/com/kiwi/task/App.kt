package com.kiwi.task

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.kiwi.task.repository.db.KiwiDatabse

class App : Application() {

    companion object{
        lateinit var context: Context
        var db: KiwiDatabse? = null

        fun isNetworkAvailable() : Boolean{
            val connectivityManager : ConnectivityManager = App.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetwork != null && connectivityManager.activeNetworkInfo?.isConnected ?: false
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        db = KiwiDatabse(this)
    }

}
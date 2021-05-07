package com.kiwi.task.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kiwi.task.BuildConfig
import com.kiwi.task.utils.formatDate
import com.kiwi.task.utils.formatDay
import com.kiwi.task.utils.formatTime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "flights")
data class Flight (
    @PrimaryKey var id: String,
    var price: Double,
    @SerializedName("fly_duration")var duration: String,
    var distance: Double,
    var cityTo: String,
    var cityFrom: String,
    var mapIdto: String?,
    var dTime: Long,
    var aTime: Long,
    @SerializedName("deep_link") var deepLink: String
) : Serializable {

    var displayedAt : Date? = null

    init{
        displayedAt = Date()
    }

    fun imageUrl() : String {
        return "${BuildConfig.IMAGES_URL}${mapIdto}${BuildConfig.IMG_EXTENSION}"
    }

    fun formatPrice(): String {
        return String.format("%.2f €", price)
    }

    fun formatDistance(): String{
        return "${distance}km"
    }

    fun formatDTime(): String{
        return dTime.formatTime()
    }

    fun formatDDay(): String{
        return dTime.formatDay()
    }

    fun formatATime(): String{
        return aTime.formatTime()
    }

    fun formatADay(): String{
        return aTime.formatDay()
    }
}



package com.kiwi.task.models

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kiwi.task.BuildConfig
import com.kiwi.task.utils.formatDay
import com.kiwi.task.utils.formatTime
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "flights")
data class Flight (
    @PrimaryKey var id: String,
    var price: Double,
    var quality: Double,
    @SerializedName("fly_duration")var duration: String,
    var distance: Double,
    var cityTo: String,
    var cityFrom: String,
    var flyFrom: String,
    var flyTo: String?,
    var mapIdfrom: String?,
    var mapIdto: String?,
    var dTimeUTC: Long,
    var aTimeUTC: Long,
    var dTime: Long,
    var aTime: Long,
    @SerializedName("deep_link") var deepLink: String
) : Serializable {

    fun imageUrl() : String {
        return "${BuildConfig.IMAGES_URL}${mapIdto}${BuildConfig.IMG_EXTENSION}"
    }

    fun formatPrice(): String{
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



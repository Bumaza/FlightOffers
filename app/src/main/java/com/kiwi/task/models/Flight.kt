package com.kiwi.task.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kiwi.task.BuildConfig
import java.io.Serializable

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
    var dTimeUTC: Long?,
    var aTimeUTC: Long?,
    var dTime: Long?,
    var aTime: Long
) : Serializable {

    fun imageURL() : String {

        return "${BuildConfig.IMAGES_URL}${mapIdfrom}{${BuildConfig.IMG_EXTENSION}"
    }
}



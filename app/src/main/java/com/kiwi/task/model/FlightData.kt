package com.kiwi.task.model

data class FlightData (
    var id: String,
    var price: Double,
    var quality: Double,
    var fly_duration: String,
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
)



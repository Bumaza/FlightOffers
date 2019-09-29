package com.kiwi.task.api

import com.kiwi.task.model.FlightData

data class FlightDataResponse (
    var search_id: String,
    var currency: String,
    var data: Array<FlightData>
)



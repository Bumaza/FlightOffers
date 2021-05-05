package com.kiwi.task.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class SearchResult (
    @SerializedName("search_id") var id: String,
    @SerializedName("data") var flights: Array<Flight>,
    var currency: String
) : Serializable



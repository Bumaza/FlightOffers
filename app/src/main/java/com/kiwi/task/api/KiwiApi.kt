package com.kiwi.task.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface KiwiApi {

    companion object {

        val kiwiApiService by lazy {
            create()
        }

        val kiwiApiImageService by lazy{
            createImageApi()
        }

        val query: MutableMap<String, String> = mutableMapOf("v" to "2", "sort" to "popularity", "asc" to "0",
            "locale" to "en", "children" to "0", "infants" to "0",
            "flyFrom" to "49.2-16.61-250km", "to" to "anywhere",
            "featureName" to "aggregateResults", "dateFrom" to "30/09/2019",
            "dateTo" to "29/09/2019", "typeFlight" to "oneway", "partner" to "picky",
            "one_per_date" to "0", "oneforcity" to "1", "wait_for_refresh" to "0",
            "adults" to "1", "limit" to "5")

        private fun create(): KiwiApi {

            return Retrofit.Builder()
                .baseUrl("https://api.skypicker.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(KiwiApi::class.java)

        }


        private fun createImageApi(): KiwiApi{
            return Retrofit.Builder()
                .baseUrl("https://images.kiwi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(KiwiApi::class.java)
        }
    }

    @GET("/photos/600x330/{mapId}.jpg")
    fun getImage(@Path("mapId", encoded = true) mapId: String) : Observable<ResponseBody>

    @GET("/flights")
    fun getPopularFlights(@QueryMap params: Map<String, String>) : Observable<FlightDataResponse>
}
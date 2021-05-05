package com.kiwi.task.repository.api

import com.kiwi.task.BuildConfig
import com.kiwi.task.models.SearchResult
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
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

        val query: MutableMap<String, String> = mutableMapOf(
            "v" to BuildConfig.API_VERSION.toString(),
            "sort" to "popularity",
            "asc" to "0",
            "locale" to "en",
            "children" to "0",
            "infants" to "0",
            "flyFrom" to "49.2-16.61-250km",
            "to" to "anywhere",
            "featureName" to "aggregateResults",
            "dateFrom" to "10/05/2021",
            "dateTo" to "29/05/2021",
            "typeFlight" to "oneway",
            "partner" to "picky",
            "one_per_date" to "0",
            "oneforcity" to "1",
            "wait_for_refresh" to "0",
            "adults" to "1",
            "limit" to "5",
            "partner" to "skypicker-android")

        private fun create(): KiwiApi {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(KiwiApi::class.java)

        }


        private fun createImageApi(): KiwiApi{
            return Retrofit.Builder()
                .baseUrl(BuildConfig.IMAGES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(KiwiApi::class.java)
        }
    }

    @GET("photos/600x330/{mapId}.jpg")
    fun getImage(@Path("mapId", encoded = true) mapId: String) : Observable<ResponseBody>

    @GET("flights")
    fun getPopularFlights(@QueryMap params: Map<String, String>) : Observable<SearchResult>
}
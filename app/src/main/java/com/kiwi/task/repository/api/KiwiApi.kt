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

    enum class FlightParams(val value: String) {
        VERSION("v"),
        SORT("sort"),
        ASC("asc"),
        LOCALE("locale"),
        PARTNER("partner"),
        LIMIT("limit"),
        CHILDREN("children"),
        INFANTS("infants"),
        FLY_FROM("flyFrom"),
        TO("to"),
        FEATURE_NAME("featureName"),
        DATE_FROM("dateFrom"),
        DATE_TO("dateTo"),
        FLIGHT_TYPE("typeFlight"),
        ONE_PER_DATE("one_per_date"),
        ONE_FOR_CITY("oneforcity"),
        WAIT_FOR_REFRESH("wait_for_refresh"),
        ADULTS("adults"),
    }

    companion object {

        val service by lazy {
            create()
        }

        val kiwiApiImageService by lazy{
            createImageApi()
        }


        val defaultQuery: MutableMap<String, String> = mutableMapOf(
            FlightParams.VERSION.value to BuildConfig.API_VERSION.toString(),
            FlightParams.SORT.value to "popularity",
            FlightParams.ASC.value to "0",
            FlightParams.LOCALE.value to "en",
            FlightParams.CHILDREN.value to "0",
            FlightParams.INFANTS.value to "0",
            FlightParams.FLY_FROM.value to "49.2-16.61-250km",
            FlightParams.TO.value to "anywhere",
            FlightParams.FEATURE_NAME.value to "aggregateResults",
            FlightParams.DATE_FROM.value to "10/05/2021",
            FlightParams.DATE_TO.value to "29/05/2021",
            FlightParams.FLIGHT_TYPE.value to "oneway",
            FlightParams.ONE_PER_DATE.value to "0",
            FlightParams.ONE_FOR_CITY.value to "1",
            FlightParams.WAIT_FOR_REFRESH.value  to "0",
            FlightParams.ADULTS.value to "1",
            FlightParams.LIMIT.value to "5",
            FlightParams.PARTNER.value to "skypicker-android")

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
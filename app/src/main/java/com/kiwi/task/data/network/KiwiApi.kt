package com.kiwi.task.data.network

import com.kiwi.task.BuildConfig
import com.kiwi.task.models.SearchResult
import com.kiwi.task.utils.formatDate
import com.kiwi.task.utils.nextMonth
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*


interface KiwiApi {

    @GET("flights")
    fun getPopularFlights(@QueryMap params: Map<String, String> = defaultQuery) : Observable<SearchResult>

    companion object {

        val service by lazy {
            create()
        }

        val defaultQuery: MutableMap<String, String> = mutableMapOf(
            FlightParams.VERSION.value to BuildConfig.API_VERSION.toString(),
            FlightParams.SORT.value to "popularity",
            FlightParams.ASC.value to "0",
            FlightParams.LOCALE.value to Locale.getDefault().language,
            FlightParams.FLY_FROM.value to Locale.getDefault().country,
            FlightParams.TO.value to "anywhere",
            FlightParams.FEATURE_NAME.value to "aggregateResults",
            FlightParams.DATE_FROM.value to Date().formatDate(), //today
            FlightParams.DATE_TO.value to Date().nextMonth().formatDate(), //one month
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build().create(KiwiApi::class.java)

        }
    }
}

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
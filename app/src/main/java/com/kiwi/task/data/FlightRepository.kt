package com.kiwi.task.data

import android.util.Log
import com.kiwi.task.models.Flight
import com.kiwi.task.data.local.FlightsDao
import com.kiwi.task.data.network.FlightParams
import com.kiwi.task.data.network.KiwiApi
import com.kiwi.task.utils.PreferencesKit
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.min

class FlightRepository(private val flightDao: FlightsDao) {

    private val TAG = "FlightRepository"
    private val limit = 5

    // Dirichlet's principle
    private var apiLimit = limit * PreferencesKit.getLimitToFetch()

    fun getFlights() : Observable<List<Flight>>{
        if(PreferencesKit.shouldFetchNewData()){
            return fetchFlightsFromApi()
        }
        return getFlightsFromDb()
    }

    private fun getFlightsFromDb() : Observable<List<Flight>> {
        return flightDao.getDailyFlights()
    }

    private fun fetchFlightsFromApi() : Observable<List<Flight>> {
        //update kiwi
        KiwiApi.defaultQuery[FlightParams.LIMIT.value] = apiLimit.toString()
        return KiwiApi.service.getPopularFlights()
            .map { result -> result.flights}
            .map { it.filter {
                    //remove duplicated destination
                    item -> flightDao.notExists(item.cityTo)}
                    //cut array to required length
                    .subList(0, min(it.size, limit))
            }
            .doOnNext{
                Log.d(TAG, "Fetched ${it.size}")
                storeFlightsInDb(it.toTypedArray())
            }
    }

    fun storeFlightsInDb(flights: Array<Flight>){
        Observable.fromCallable {  flightDao.insert(flights)}
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                PreferencesKit.updateFetchFlag()
                Log.d(TAG, "Inserted ${flights.size} flights from Api in Db.")
            }
    }

}
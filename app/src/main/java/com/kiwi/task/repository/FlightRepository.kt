package com.kiwi.task.repository

import com.kiwi.task.App
import com.kiwi.task.models.Flight
import io.reactivex.Observable

class FlighRepository {

    fun getFlights() : Observable<ArrayList<Flight>>{
        
    }
    
    fun getFlightsFromDb() : Observable<ArrayList<Flight>>{
        return App.db?.gDao()?.getAllFlights()?.filter { it.isNotEmpty() }!!.toObservable()
    }
    
    fun storeFlightsI

}
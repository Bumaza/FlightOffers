package com.kiwi.task.viewmodels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.kiwi.task.App
import com.kiwi.task.data.FlightRepository
import com.kiwi.task.models.Flight
import com.kiwi.task.data.network.KiwiApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class TopOffersViewModel : BaseViewModel() {

    val TAG = "TopOffers"

    var flightRepo: FlightRepository = FlightRepository(App.db?.gDao()!!)

    var topFlights: MutableLiveData<Array<Flight>> = MutableLiveData()
    var status : MutableLiveData<Status> = MutableLiveData()

    var currentFlight: MutableLiveData<Flight> = MutableLiveData()

    init{
        fetchFlights()
    }

    fun fetchFlights(limit: Int = 5){
        compositeDisposable.add(
            flightRepo.getFlights()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> status.value = Status.LOADING }
                .subscribe({
                    result ->
                    status.value = Status.SUCCESS
                    result.forEach {
                        Log.d("CITY", it.cityTo)
                    }
                    topFlights.postValue(result.toTypedArray())
                },
                    { error ->
                        status.value = Status.ERROR
                        error.message?.let { println(it) }
                    }
                )
            )
    }


}
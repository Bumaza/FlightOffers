package com.kiwi.task.viewmodels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.kiwi.task.models.Flight
import com.kiwi.task.repository.api.KiwiApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TopOffersViewModel : BaseViewModel() {

    val TAG = "TopOffers"

    var isChange = ObservableBoolean(false)
    var isAscOrder = ObservableBoolean(false)

    var topFlights: MutableLiveData<Array<Flight>> = MutableLiveData()
    var currentQueries: MutableMap<String, String> = mutableMapOf()

    init{
        currentQueries = KiwiApi.defaultQuery
    }

    fun fetchFlights(limit: Int = 5){
        compositeDisposable.add(
            KiwiApi.service.getPopularFlights(currentQueries)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result ->  topFlights.postValue(result.flights)
                },
                    { error ->
                        error.message?.let { println(it) }
                    }
                )
            )
    }

    fun onAscSelect(){

    }

    fun onDescSelect(){

    }


    fun applyFilter(){
        Log.d(TAG, "Apply")
    }

    fun onFilterClose(){

    }

}
package com.kiwi.task.viewmodels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.kiwi.task.models.Flight
import com.kiwi.task.data.network.KiwiApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class TopOffersViewModel : BaseViewModel() {

    val TAG = "TopOffers"

    var isChange = ObservableBoolean(false)
    var isAscOrder = ObservableBoolean(false)

    var topFlights: MutableLiveData<Array<Flight>> = MutableLiveData()
    var currentQueries: MutableMap<String, String> = mutableMapOf()

    var isLoading = ObservableBoolean(true)
    var today = Date()

    var status : MutableLiveData<Status> = MutableLiveData()

    init{
        currentQueries = KiwiApi.defaultQuery
        fetchFlights()
    }

    fun fetchFlights(limit: Int = 5){
        isLoading.set(true)
        compositeDisposable.add(
            KiwiApi.service.getPopularFlights(currentQueries)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> status.value = Status.LOADING }
                .subscribe({
                    result ->
                    status.value = Status.SUCCESS
                    topFlights.postValue(result.flights)
                    isLoading.set(false)
                },
                    { error ->
                        status.value = Status.ERROR
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
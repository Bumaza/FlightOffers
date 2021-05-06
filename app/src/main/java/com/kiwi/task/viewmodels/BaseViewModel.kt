package com.kiwi.task.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.kiwi.task.App
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected var isNetworkAvailable = ObservableBoolean(false)
    protected val compositeDisposable = CompositeDisposable()

    init{
        isNetworkAvailable.set(App.isNetworkAvailable())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    open fun onRefreshNetwork(){}
}
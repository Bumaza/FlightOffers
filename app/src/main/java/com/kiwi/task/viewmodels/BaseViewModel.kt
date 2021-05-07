package com.kiwi.task.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.kiwi.task.App
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
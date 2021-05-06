package com.kiwi.task.utils

import android.content.Context
import com.kiwi.task.App

object PreferencesKit {

    const val SHARED_PREFS_NAME = "flights"
    const val SHOULD_FETCH_DATA = "should_fetch"

    /**
     * Flag if to fetch new data
     */

    fun shouldFetchNewData(): Boolean{
        return App.context.getBooleanPref(SHOULD_FETCH_DATA)
    }

    fun updateFetchFlag(flag: Boolean) {
        App.context.putBooleanPref(SHOULD_FETCH_DATA, flag)
    }
}
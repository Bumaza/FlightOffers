package com.kiwi.task.utils

import android.content.Context
import com.kiwi.task.App
import java.util.*

object PreferencesKit {

    const val SHARED_PREFS_NAME = "flights"
    const val SHOULD_FETCH_DATA = "should_fetch"
    const val LIMIT_TO_FETCH = "fetch_limit"

    /**
     * Flag if to fetch new data
     */

    fun shouldFetchNewData(): Boolean{
        return App.context.getLongPref(SHOULD_FETCH_DATA).isNotToday()
    }

    fun updateFetchFlag() {
        App.context.putLongPref(SHOULD_FETCH_DATA, Date().time)
        App.context.incrementLongPref(LIMIT_TO_FETCH)
    }

    fun getLimitToFetch(): Long{
        return App.context.getLongPref(LIMIT_TO_FETCH)
    }
}
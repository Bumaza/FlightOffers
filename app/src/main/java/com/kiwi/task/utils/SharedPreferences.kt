package com.kiwi.task.utils

import android.content.Context
import android.content.SharedPreferences
import com.kiwi.task.model.FlightData

class SharedPreferences(val context: Context) {

    private val PREFS_NAME = "kiwi_flights"

    private val KEY_NAME = "flight_id"

    val sharedPref: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()

        val newSet = getStringSet()
        newSet?.add(value)

        editor.putStringSet(KEY_NAME, newSet)
        editor.commit()
    }

    fun save(data: ArrayList<FlightData>){
        val editor: SharedPreferences.Editor = sharedPref.edit()

        val newSet = getStringSet()
        for(flight in data){
            newSet?.add(flight.id)
        }

        editor.putStringSet(KEY_NAME, newSet)
        editor.commit()
    }

    fun getStringSet(): MutableSet<String>? {
        return sharedPref.getStringSet(KEY_NAME, null)
    }

    fun save(key: String, value: Long){
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putLong(key, value)
        editor.commit()
    }

    fun getLongValue(key: String): Long? {
        return sharedPref.getLong(key, -1)
    }

    fun removeValue(KEY_NAME: String) {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.remove(KEY_NAME)
        editor.commit()
    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.clear()
        editor.commit()
    }


}
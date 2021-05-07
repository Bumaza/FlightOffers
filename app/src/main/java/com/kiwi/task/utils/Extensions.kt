package com.kiwi.task.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

/***
 * Date formatter for UI and API calls
 */
val dayFormatter = SimpleDateFormat("dd.MM")
val timeFormatter = SimpleDateFormat("hh:mm")
val dateFormatter = SimpleDateFormat("dd/MM/yyyy")



/***
 * Date extension to print formatted Date, etc...
 */


fun Date.formatDay() : String {
    return dayFormatter.format(this)
}

fun Date.formatTime() : String {
    return timeFormatter.format(this)
}

fun Date.formatDate() : String {
    return dateFormatter.format(this)
}

fun Date.nextMonth() : Date{
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, 1)
    this.time = cal.time.time
    return this
}


/***
 * Long extension to print formatted Date from timestamp
 */

fun Long.formatDate() : String {
    return dateFormatter.format(Date(this * 1000L))
}

fun Long.formatDay() : String {
    return dayFormatter.format(Date(this * 1000L))
}

fun Long.formatTime() : String {
    return timeFormatter.format(Date(this * 1000L))
}

fun Long.isNotToday(): Boolean {
    return Date(this).formatDate() != Date().formatDate()
}

/***
 * Context extension for Preferences
 */


fun Context.putLongPref(key: String, value: Long){
    getSharedPreferences(PreferencesKit.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putLong(key, value)?.apply()
}

fun Context.getLongPref(key: String) : Long {
    return getSharedPreferences(PreferencesKit.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        .getLong(key, 1)
}

fun Context.incrementLongPref(key: String){
    getSharedPreferences(PreferencesKit.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putLong(key, getLongPref(key)+1)?.apply()
}

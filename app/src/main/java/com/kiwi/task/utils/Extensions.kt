package com.kiwi.task.utils

import java.text.SimpleDateFormat
import java.util.*

val dayFormatter = SimpleDateFormat("dd.MM")
val timeFormatter = SimpleDateFormat("hh:mm")
val dateFormatter = SimpleDateFormat("dd/MM/yyyy")

fun Date.formatDay() : String {
    return dayFormatter.format(this)
}

fun Date.formatTime() : String {
    return dayFormatter.format(this)
}

fun Date.formatDate() : String {
    return dateFormatter.format(this)
}

fun Long.formatDate() : String {
    return dateFormatter.format(Date(this))
}

fun Long.formatDay() : String {
    return dayFormatter.format(Date(this))
}

fun Long.formatTime() : String {
    return dayFormatter.format(Date(this))
}
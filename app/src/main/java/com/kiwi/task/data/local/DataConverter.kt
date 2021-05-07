package com.kiwi.task.data.local

import androidx.room.TypeConverter
import com.kiwi.task.utils.formatDate
import java.util.*

class DataConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?) : Date? {
        return value?.let{ Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time ?: Date().time
    }

}
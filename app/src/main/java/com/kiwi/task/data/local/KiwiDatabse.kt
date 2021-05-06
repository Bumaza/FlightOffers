package com.kiwi.task.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kiwi.task.models.Flight

@Database(entities= [Flight::class], version=1, exportSchema = false)

abstract class KiwiDatabse : RoomDatabase() {

    abstract fun gDao(): FlightsDao

    companion object{
        @Volatile private var instance: KiwiDatabse? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            KiwiDatabse::class.java, "kiwi.db")
            .fallbackToDestructiveMigration().build()
    }

}
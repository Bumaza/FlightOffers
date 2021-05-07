package com.kiwi.task.data.local

import androidx.room.*
import com.kiwi.task.models.Flight
import io.reactivex.rxjava3.core.Observable
import java.util.*

@Dao
interface FlightsDao {

    @Query("SELECT * from flights")
    fun getAllFlights(): List<Flight>

    @Query("SELECT * from flights WHERE displayedAt=:day")
    fun getDailyFlights(day: Date = Date()): Observable<List<Flight>>

    @Query("SELECT * FROM flights WHERE id=:flightId")
    fun getFlight(flightId: Int) : Flight

    @Query("SELECT NOT EXISTS (SELECT 1 FROM flights WHERE cityTo=:destination)")
    fun notExists(destination: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flights: Array<Flight>)

    @Delete
    fun delete(vararg  flight: Flight)

}
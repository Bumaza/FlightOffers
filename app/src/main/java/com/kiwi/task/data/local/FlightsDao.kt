package com.kiwi.task.data.local

import androidx.room.*
import com.kiwi.task.models.Flight
import io.reactivex.rxjava3.core.Observable
import java.util.*

@Dao
interface FlightsDao {

    @Query("SELECT * from flights")
    fun getAllFlights(): Observable<List<Flight>>

    @Query("SELECT * from flights ORDER BY displayedAt DESC LIMIT :limit")
    fun getDailyFlights(limit: Int): Observable<List<Flight>>

    @Query("SELECT * FROM flights WHERE id=:flightId")
    fun getFlight(flightId: Int) : Flight

    @Query("SELECT NOT EXISTS (SELECT 1 FROM flights WHERE cityTo=:destination)")
    fun notExists(destination: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flights: Array<Flight>)

    @Delete
    fun delete(vararg  flight: Flight)

}
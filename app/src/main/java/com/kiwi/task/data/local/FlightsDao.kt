package com.kiwi.task.data.local

import androidx.room.*
import com.kiwi.task.models.Flight

@Dao
interface FlightsDao {

    @Query("SELECT * from flights")
    fun getAllFlights(): List<Flight>

    @Query("SELECT * FROM flights WHERE id=:flightId")
    fun getFlight(flightId: Int) : Flight

    @Query("SELECT EXISTS (SELECT 1 FROM flights WHERE cityTo=:destination)")
    fun exists(destination: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg flight: Flight)

    @Delete
    fun delete(vararg  flight: Flight)

}
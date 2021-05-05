package com.kiwi.task.repository.db

import androidx.room.*
import com.kiwi.task.models.Flight

@Dao
interface FlightsDao {

    @Query("SELECT * from flights")
    fun getAllFlights(): List<Flight>

    @Query("SELECT * FROM flights WHERE id=:flightId")
    fun getFlight(flightId: Int) : Flight

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg flight: Flight)

    @Delete
    fun delete(vararg  flight: Flight)

}
package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids")
    fun getAsteroids(): List<Asteroid>

    @Query("SELECT * FROM asteroids where epochDateCloseApproach> :timeNowInMillisMinusOneDay Order BY epochDateCloseApproach ASC")
    fun getFilteredAsteroids(timeNowInMillisMinusOneDay: Long): List<Asteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(asteroid: Asteroid)
}
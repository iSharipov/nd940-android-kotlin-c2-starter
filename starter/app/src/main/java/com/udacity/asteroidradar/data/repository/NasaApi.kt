package com.udacity.asteroidradar.data.repository

import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("/neo/rest/v1/feed")
    fun fetchAsteroids(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): Call<String>

    @GET("/planetary/apod")
    fun pictureOfDay(
        @Query("api_key") apiKey: String
    ): Call<PictureOfDay>
}
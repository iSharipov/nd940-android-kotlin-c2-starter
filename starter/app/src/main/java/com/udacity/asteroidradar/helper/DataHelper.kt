package com.udacity.asteroidradar.helper

import android.content.Context
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidRadarApp
import com.udacity.asteroidradar.AsteroidsWrapper
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.data.repository.NasaApi
import com.udacity.asteroidradar.util.SingletonHolder
import org.json.JSONObject
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataHelper private constructor(context: Context) {
    private var nasaApi: NasaApi = getRetrofit().create(NasaApi::class.java)

    companion object : SingletonHolder<DataHelper, Context>(::DataHelper) {
        private fun getRetrofit(): Retrofit = AsteroidRadarApp.instance.provideRetrofit()
    }

    fun fetchAsteroids(): AsteroidsWrapper {
        try {
            loadAsteroids().also {
                return AsteroidsWrapper().apply {
                    setAsteroids(it)
                }
            }
        } catch (ex: IOException) {
            Timber.e(ex, "Error while asteroids request")
            return AsteroidsWrapper().apply {
                setAsteroids((emptyList()))
            }
        }
    }

    fun loadAsteroids(): List<Asteroid> {
        return parseAsteroidsJsonResult(JSONObject(nasaApi.fetchAsteroids(
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault()).format(
                Date()
            ), BuildConfig.API_KEY).execute().body()!!))
    }

    fun pictureOfDay(): PictureOfDay? {
        return try {
            nasaApi.pictureOfDay(BuildConfig.API_KEY).execute().also {
                if (it.isSuccessful) {
                    it.body()!!
                } else {
                    Timber.e(it.errorBody().toString())
                }
            }.body()
        } catch (ex: IOException) {
            Timber.e(ex, "Error while picture of day request")
            null
        }
    }
}
package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.AsteroidsWrapper
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.helper.DataHelper
import com.udacity.asteroidradar.repository.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val asteroids: MutableLiveData<AsteroidsWrapper> = MutableLiveData()
    private val pictureOfDay: MutableLiveData<PictureOfDay> = MutableLiveData()

    fun getAsteroids() = asteroids

    fun getPictureOfDay() = pictureOfDay

    suspend fun fetchAsteroids() {
            withContext(Dispatchers.IO) {
                DataHelper.getInstance(getApplication()).fetchAsteroids(SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault()).format(Date()))
            }.also {
                if (it.getAsteroids().isEmpty()) {
                    AppDatabase.getAppDataBase(context = getApplication())?.let { db ->
                        val calendar = Calendar.getInstance()
                        calendar.time = Date()
                        calendar.add(Calendar.DATE, -1)
                        it.setAsteroids(db.asteroidDao().getFilteredAsteroids(calendar.timeInMillis))
                    }
                } else {
                    AppDatabase.getAppDataBase(context = getApplication())?.let { db ->
                        it.getAsteroids().forEach { asteroid ->
                            db.asteroidDao().insertAsteroid(asteroid)
                        }
                    }
                }
                asteroids.value = it
            }
    }

    suspend fun fetchPictureOfDay() {
        withContext(Dispatchers.IO) {
            DataHelper.getInstance(getApplication()).pictureOfDay()
            }.also {
                pictureOfDay.value = it
            }
    }
}
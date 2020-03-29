package com.udacity.asteroidradar.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.AsteroidsWrapper
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.helper.DataHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(private val dataHelper: DataHelper) : ViewModel() {

    private val asteroids: MutableLiveData<AsteroidsWrapper> = MutableLiveData()
    private val pictureOfDay: MutableLiveData<PictureOfDay> = MutableLiveData()

    fun getAsteroids() = asteroids

    fun getPictureOfDay() = pictureOfDay

    fun fetchAsteroids() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                dataHelper.fetchAsteroids(SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault()).format(Date()))
            }.also {
                asteroids.value = it
            }
        }
    }

    fun fetchPictureOfDay() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                dataHelper.pictureOfDay()
            }.also {
                pictureOfDay.value = it
            }
        }
    }
}
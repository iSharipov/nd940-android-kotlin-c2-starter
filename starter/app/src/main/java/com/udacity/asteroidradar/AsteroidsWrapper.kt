package com.udacity.asteroidradar

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class AsteroidsWrapper : BaseObservable() {

    private lateinit var asteroids: List<Asteroid>

    fun setAsteroids(asteroids: List<Asteroid>) {
        this.asteroids = asteroids
        notifyPropertyChanged(BR.asteroids)
    }

    @Bindable
    fun getAsteroids() = asteroids
}
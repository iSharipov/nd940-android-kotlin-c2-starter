package com.udacity.asteroidradar.quartz

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.helper.DataHelper
import com.udacity.asteroidradar.repository.AppDatabase
import timber.log.Timber

class AsteroidsDownloadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            DataHelper.getInstance(applicationContext).loadAsteroids().also {
                AppDatabase.getAppDataBase(context = applicationContext)?.let { db ->
                    it.forEach { asteroid ->
                        db.asteroidDao().insertAsteroid(asteroid)
                    }
                }
            }
            Result.success()
        } catch (ex: Exception) {
            Timber.e("Error while asteroids background request")
            Result.failure()
        }
    }
}
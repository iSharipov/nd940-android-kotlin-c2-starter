package com.udacity.asteroidradar

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType.CONNECTED
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.quartz.AsteroidsDownloadWorker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.SECONDS

class AsteroidRadarApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        provideTimber()
        provideAsteroidsWorker()
    }

    companion object {
        lateinit var instance: AsteroidRadarApp
            private set
    }

    fun provideRetrofit(): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.connectTimeout(3, SECONDS)
        httpClient.readTimeout(3, SECONDS)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.interceptors().add(logging)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
            .build()
    }

    private fun provideTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun provideAsteroidsWorker() {
        PeriodicWorkRequestBuilder<AsteroidsDownloadWorker>(24, HOURS).apply {
            setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(CONNECTED)
                    .setRequiresCharging(true)
                    .build()
            )
        }.also {
            WorkManager.getInstance().enqueue(it.build())
        }
    }
}
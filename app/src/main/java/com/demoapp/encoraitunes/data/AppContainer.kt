package com.demoapp.encoraitunes.data

import android.app.Application
import com.demoapp.encoraitunes.data.db.AppDatabase
import com.demoapp.encoraitunes.data.network.HttpInterceptor
import com.demoapp.encoraitunes.data.network.ITunesApiService
import okhttp3.OkHttpClient
import com.demoapp.encoraitunes.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * Here we define application level dependency
 */
class AppContainer(context: Application) {
    /**
     * instance of database
     */
    val appDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    /**
     * instance of reddit api
     */
    val iTunesApiService by lazy {
        ITunesApiService.create(getOkHttpClient())
    }


    /**
     * OkHttp performs best when you create a single `OkHttpClient` instance and reuse it for all of
     * your HTTP calls. This is because each client holds its own connection pool and thread pools.
     * Reusing connections and threads reduces latency and saves memory. Conversely, creating a client
     * for each request wastes resources on idle pools.
     * @return OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .addInterceptor(HttpInterceptor())
            .build()
    }

    /**
     * An OkHttp interceptor which logs request and response information. Can be applied as an
     * [application interceptor][OkHttpClient.interceptors] or as a [OkHttpClient.networkInterceptors].
     *
     * The format of the logs created by this class should not be considered stable and may
     * change slightly between releases. If you need a stable logging format, use your own interceptor.
     */
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG)
            logger.level = HttpLoggingInterceptor.Level.BODY
        else
            logger.level = HttpLoggingInterceptor.Level.NONE
        return logger
    }
}
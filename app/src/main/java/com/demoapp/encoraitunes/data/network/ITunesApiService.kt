package com.demoapp.encoraitunes.data.network

import com.demoapp.encoraitunes.data_classes.TopSongs
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ITunesApiService {
    @GET("ws/RSS/topsongs/limit={limit}/json")
    fun fetchSongListsAsync(
        @Path("limit") page: Int,
    ): Deferred<Response<TopSongs>>

    companion object {
        private const val BASE_URL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/"

        fun create(okHttpClient: OkHttpClient): ITunesApiService = create(
            BASE_URL.toHttpUrlOrNull()!!,
            okHttpClient
        )

        fun create(httpUrl: HttpUrl, okHttpClient: OkHttpClient): ITunesApiService {
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ITunesApiService::class.java)
        }
    }
}
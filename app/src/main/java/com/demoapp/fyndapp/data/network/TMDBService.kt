package com.demoapp.fyndapp.data.network

import com.demoapp.fyndapp.data_classes.MoviesList
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {
    @GET("discover/movie?sort_by=popularity.desc")
    fun fetchMovieListAsync(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String,
    ): Deferred<Response<MoviesList>>

    @GET("search/movie")
    fun searchMoviesAsync(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
    ): Deferred<Response<MoviesList>>


    companion object {
        private const val BASE_URL_2 = "http://api.themoviedb.org/3/"

        fun create(okHttpClient: OkHttpClient): TMDBService = create(
            BASE_URL_2.toHttpUrlOrNull()!!,
            okHttpClient
        )

        fun create(httpUrl: HttpUrl, okHttpClient: OkHttpClient): TMDBService {
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TMDBService::class.java)
        }
    }
}
package com.demoapp.fyndapp.util.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demoapp.fyndapp.data.db.AppDatabase
import com.demoapp.fyndapp.data.network.ApiCallback
import com.demoapp.fyndapp.data.network.ApiRepository
import com.demoapp.fyndapp.data.network.TMDBService
import com.demoapp.fyndapp.data_classes.Movie
import com.demoapp.fyndapp.data_classes.MoviesList
import com.demoapp.fyndapp.util.ApiErrorModel
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val apiService: TMDBService,
    private val localDb: AppDatabase
) : ViewModel() {
    val apiResponse = MutableLiveData<Any>()
    val isEmptyList = ObservableField(false)
    val searchQuery = ObservableField("")
    val loading = ObservableField(false)
    var list = localDb.moviesDao().getAllMovies()
    var page = 1
    var totalRecords = 0
    var totalPages = 0

    fun getTopMovies() {
        loading.set(true)
        val apiRequest =
            apiService.fetchMovieListAsync(page, "1016b2ced3177dd0562889cfafe3b3cc")
        viewModelScope.launch {
            ApiRepository.callApi(apiRequest, object : ApiCallback<MoviesList> {
                override fun onException(error: Throwable) {
                    loading.set(true)
                    apiResponse.value = error
                }

                override fun onError(errorModel: ApiErrorModel) {
                    loading.set(true)
                    apiResponse.value = errorModel
                }

                override fun onSuccess(t: MoviesList?) {
                    loading.set(false)
                    totalRecords = t?.totalResults ?: 0
                    totalPages = t?.totalPages ?: 0
                    t?.results?.let {
                        insertMoviesToDb(it)
                    } ?: isEmptyList.set(true)
                    apiResponse.value = t
                }
            })
        }
    }

    fun searchMovie(query: String) {
        val apiRequest =
            apiService.searchMoviesAsync(page, query, "1016b2ced3177dd0562889cfafe3b3cc")
        viewModelScope.launch {
            ApiRepository.callApi(apiRequest, object : ApiCallback<MoviesList> {
                override fun onException(error: Throwable) {
                    loading.set(true)
                    apiResponse.value = error
                }

                override fun onError(errorModel: ApiErrorModel) {
                    loading.set(true)
                    apiResponse.value = errorModel
                }

                override fun onSuccess(t: MoviesList?) {
                    loading.set(false)
                    totalRecords = t?.totalResults ?: 0
                    totalPages = t?.totalPages ?: 0
                    t?.results?.let {
                        insertMoviesToDb(it)
                    } ?: isEmptyList.set(true)
                    apiResponse.value = t
                }
            })
        }
    }

    private fun insertMoviesToDb(list: List<Movie>) {
        viewModelScope.launch {
            localDb.moviesDao().deleteAndInsert(list)
        }
    }
}
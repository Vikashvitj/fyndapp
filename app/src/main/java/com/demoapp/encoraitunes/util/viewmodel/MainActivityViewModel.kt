package com.demoapp.encoraitunes.util.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demoapp.encoraitunes.data.db.AppDatabase
import com.demoapp.encoraitunes.data.network.ApiCallback
import com.demoapp.encoraitunes.data.network.ApiRepository
import com.demoapp.encoraitunes.data.network.ITunesApiService
import com.demoapp.encoraitunes.data_classes.Entry
import com.demoapp.encoraitunes.data_classes.TopSongs
import com.demoapp.encoraitunes.util.ApiErrorModel
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val iTunesApiService: ITunesApiService,
    private val localDb: AppDatabase
) : ViewModel() {
    val apiResponse = MutableLiveData<Any>()
    val isEmptyList = ObservableField(false)
    val loading = ObservableField(false)
    var list = localDb.songDao().getAllSongs()

    fun getTopSongs() {
        loading.set(true)
        val apiRequest = iTunesApiService.fetchSongListsAsync(25)
        viewModelScope.launch {
            ApiRepository.callApi(apiRequest, object : ApiCallback<TopSongs> {
                override fun onException(error: Throwable) {
                    loading.set(true)
                    apiResponse.value = error
                }

                override fun onError(errorModel: ApiErrorModel) {
                    loading.set(true)
                    apiResponse.value = errorModel
                }

                override fun onSuccess(t: TopSongs?) {
                    loading.set(false)
                    t?.feed?.let {
                        assignImageLink(it.entry)
                        insertSongsToDb(it.entry)
                    } ?: isEmptyList.set(true)
                    apiResponse.value = t
                }
            })
        }
    }

    private fun assignImageLink(entry: List<Entry>) {
        entry.forEach {
            it.imageArt = it.imImage?.lastOrNull()?.label
            it.playLink = it.link?.lastOrNull()?.attributes?.href
        }
    }

    private fun insertSongsToDb(list: List<Entry>) {
        viewModelScope.launch {
            localDb.songDao().deleteAndInsert(list)
        }
    }
}
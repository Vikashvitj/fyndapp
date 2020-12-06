package com.demoapp.encoraitunes.util.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demoapp.encoraitunes.data.db.AppDatabase
import com.demoapp.encoraitunes.data_classes.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongDetailViewModel(private val songId: String, private val localDb: AppDatabase) :
    ViewModel() {

    lateinit var songItem: Entry

    init {
        viewModelScope.launch(Dispatchers.IO) {
            songItem = localDb.songDao().getSong(songId)
        }
    }


}
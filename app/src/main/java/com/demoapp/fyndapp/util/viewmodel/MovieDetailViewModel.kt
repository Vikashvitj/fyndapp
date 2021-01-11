package com.demoapp.fyndapp.util.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demoapp.fyndapp.data.db.AppDatabase
import com.demoapp.fyndapp.data_classes.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val songId: Int, private val localDb: AppDatabase) :
    ViewModel() {

    lateinit var songItem: Movie

    init {
        viewModelScope.launch(Dispatchers.IO) {
            songItem = localDb.moviesDao().getMovie(songId)
        }
    }


}
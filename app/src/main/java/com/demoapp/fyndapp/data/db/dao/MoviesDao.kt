package com.demoapp.fyndapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.demoapp.fyndapp.data_classes.Movie

@Dao
interface MoviesDao : BaseDao<Movie> {
    @Query("SELECT * FROM movies_list")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movies_list WHERE id LIKE:id")
    fun getMovie(id: Int): Movie

    @Query("DELETE FROM movies_list")
    abstract suspend fun deleteAll()


    @Transaction
    open suspend fun deleteAndInsert(movies: List<Movie>) {
        //deleteAll()
        insert(movies)
    }

}

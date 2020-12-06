package com.demoapp.encoraitunes.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.demoapp.encoraitunes.data_classes.Entry

@Dao
interface SongDao : BaseDao<Entry> {
    @Query("SELECT * FROM entry_song")
    fun getAllSongs(): LiveData<List<Entry>>

    @Query("SELECT * FROM entry_song WHERE idattributeimId LIKE:songId")
    fun getSong(songId: String): Entry

    @Query("DELETE FROM entry_song")
    abstract suspend fun deleteAll()


    @Transaction
    open suspend fun deleteAndInsert(students: List<Entry>) {
        deleteAll()
        insert(students)
    }

}

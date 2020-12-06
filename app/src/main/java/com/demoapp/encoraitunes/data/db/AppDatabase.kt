package com.demoapp.encoraitunes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demoapp.encoraitunes.data.db.dao.SongDao
import com.demoapp.encoraitunes.data_classes.DataConverter
import com.demoapp.encoraitunes.data_classes.DataLinkConverter
import com.demoapp.encoraitunes.data_classes.Entry

@Database(
    entities = [Entry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class, DataLinkConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {

        private const val DATABASE_NAME = "encora.db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

/*
*
         * methos used to get instance of AppDatabase
         * @param context to build database instance
         * @return AppDatabase
*/


        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        /*  *
          * Create and pre-populate the database.
          * @param context to build database instance
          * @ return AppDatabase
  */

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                })
                .build()
        }
    }
}

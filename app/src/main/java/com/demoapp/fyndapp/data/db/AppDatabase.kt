package com.demoapp.fyndapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demoapp.fyndapp.data.db.dao.MoviesDao
import com.demoapp.fyndapp.data_classes.GenreConverter
import com.demoapp.fyndapp.data_classes.Movie

@Database(
    entities = [Movie::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(GenreConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

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

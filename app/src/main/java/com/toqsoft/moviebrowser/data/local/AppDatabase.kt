package com.toqsoft.moviebrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.toqsoft.moviebrowser.data.dao.MovieDao
import com.toqsoft.moviebrowser.data.model.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

package com.example.dairyman.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DairyData::class, HistoryData::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun dairyDao(): DatabaseDao
}

package com.example.dairyman.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.Data.Model.HistoryData

@Database(entities = [DairyData::class, HistoryData::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun DatabaseDao(): DatabaseDao
}

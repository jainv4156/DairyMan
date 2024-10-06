package com.example.dairyman.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.data.model.HistoryData

@Database(entities = [DairyData::class, HistoryData::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun DatabaseDao(): DatabaseDao
}

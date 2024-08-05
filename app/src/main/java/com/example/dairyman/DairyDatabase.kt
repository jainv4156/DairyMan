package com.example.dairyman

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DairyData::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun dairyDao(): DatabaseDao
}

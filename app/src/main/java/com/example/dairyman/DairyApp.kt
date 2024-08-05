package com.example.dairyman

import android.app.Application
import androidx.room.Room

class DairyApp: Application() {
    companion object {
        lateinit var db: AppDatabase
    }
    override fun onCreate() {
        super.onCreate()
        db= Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "DairyTable"
        ).build()
    }


}
package com.example.dairyman

import android.app.Application
import androidx.room.Room
import com.example.dairyman.data.AppDatabase
import com.google.firebase.FirebaseApp

class DairyApp: Application() {
    companion object {
        lateinit var db: AppDatabase
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "DairyTable"
        ).build()
    }

 
}
package com.example.dairyman

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.dairyman.Data.AppDatabase
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

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
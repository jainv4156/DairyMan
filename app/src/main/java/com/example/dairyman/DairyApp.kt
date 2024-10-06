package com.example.dairyman

import android.app.Application
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dairyman.data.AppDatabase
import com.example.dairyman.worker.SyncWorker
import com.google.firebase.FirebaseApp
import java.util.concurrent.TimeUnit

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
        setUpWorker()
    }

    private fun setUpWorker() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workerRequest= PeriodicWorkRequest.Builder(SyncWorker::class.java,1,TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(workerRequest)
    }


}
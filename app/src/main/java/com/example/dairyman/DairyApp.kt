package com.example.dairyman

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.dairyman.data.AppDatabase
import com.example.dairyman.worker.SyncWorker
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

class DairyApp: Application() {
    companion object {
        lateinit var db: AppDatabase
    }
    var isAppRestart:Boolean=true
    private val Context.datastore by preferencesDataStore(name = "autoSyncUpdates")
    suspend fun saveAutoSyncUpdates(boolValue:Boolean,time: Long=System.currentTimeMillis()){
        val lastSyncTime = longPreferencesKey("lastSyncTime")
        val isSyncMessageShown= booleanPreferencesKey("isSyncMessageShown")
        datastore.edit {autoSyncUpdates->
            autoSyncUpdates[lastSyncTime]=time
            autoSyncUpdates[isSyncMessageShown]=boolValue
        }
    }

    suspend fun readLastSyncTime():Long{
        val lastSyncTime = longPreferencesKey("lastSyncTime")
        val preferences=datastore.data.first()
        val isSyncMessageShown= booleanPreferencesKey("isSyncMessageShown")
        val isSyncMessageShownValue=preferences[isSyncMessageShown]
        if(isSyncMessageShownValue==false && preferences[lastSyncTime]!=null ){
            return preferences[lastSyncTime]!!
        }
        return 0L
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
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("syncWorker",
            ExistingPeriodicWorkPolicy.KEEP,workerRequest)
    }


}
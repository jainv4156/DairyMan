package com.example.dairyman.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dairyman.viewmodel.DairyViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncWorker(context: Context, workerParams: WorkerParameters) : Worker(context,workerParams) {
    override fun doWork(): Result {
        val viewModel= DairyViewModel()
        CoroutineScope(Dispatchers.IO).launch{
            if(FirebaseAuth.getInstance().currentUser!=null) {
                viewModel.syncDataWithCloud()
            }
        }
        return Result.success()
    }

}
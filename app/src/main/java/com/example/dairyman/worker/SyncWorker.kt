package com.example.dairyman.worker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dairyman.DairyApp
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.data.model.HistoryData
import com.example.dairyman.firebase.FireStoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SyncWorker(context: Context, workerParams: WorkerParameters) : Worker(context,workerParams) {
    override fun doWork(): Result {

        CoroutineScope(IO).launch{
            if(FirebaseAuth.getInstance().currentUser!=null && isInternetAvailable(context = applicationContext)) {
                syncDataWithCloud()
                val application=applicationContext as DairyApp
                application.saveAutoSyncUpdates(false,System.currentTimeMillis())
            }
        }
        return Result.success()
    }

}
private val databaseDao= DairyApp.db.DatabaseDao()
private lateinit var customerListFromDatabase: Flow<List<DairyData>>

suspend fun syncDataWithCloud() {
        syncDairyDataWithCloud()
        syncHistoryDataWithCloud()
}
private suspend fun syncDairyDataWithCloud(){
    customerListFromDatabase = databaseDao.loadAllDairyData()
    val fireBaseDairyData:List<DairyData> =  FireStoreClass().getCustomerDairyDataFromFireStore()

    val dairyDataList:List<DairyData> = customerListFromDatabase.first()

    val fireBaseListNotInDairyData= mutableListOf<DairyData>()
    val listToUpdateFireBaseDairyData= mutableListOf<DairyData>()

    for (i in fireBaseDairyData){
        if ( dairyDataList.find {it.id ==i.id}==null){
            fireBaseListNotInDairyData.add(i)
        }
        else{
            val currentDairyData=dairyDataList.find { it.id==i.id }
            if(currentDairyData!=i){
                listToUpdateFireBaseDairyData.add(currentDairyData!!)
            }
            dairyDataList.minus(currentDairyData)
        }
    }


    if(dairyDataList.isNotEmpty()){
        FireStoreClass().addCustomerDairyDataToFireStore(dairyDataList)
        for(dairyData in dairyDataList){
            databaseDao.upsertDairyData(dairyData.copy(isSynced = true))
        }
    }

    if(dairyDataList.find { it.isSynced } !=null){
        if(listToUpdateFireBaseDairyData.isNotEmpty()) {
            FireStoreClass().updateDiaryDataToFireStore(listToUpdateFireBaseDairyData)
        }
        if(fireBaseListNotInDairyData.isNotEmpty()){
            FireStoreClass().deleteDairyDataFromFireStore(fireBaseListNotInDairyData)
        }
    }
    else{
        if(fireBaseListNotInDairyData.isNotEmpty()){
            for( i in fireBaseListNotInDairyData) {
                databaseDao.upsertDairyData(i)
            }
        }
    }
}
private suspend fun syncHistoryDataWithCloud() {
    val fireBaseHistoryData:List<HistoryData> = FireStoreClass().getCustomerHistoryDataFromFireStore()

    val historyDataList:List<HistoryData> =databaseDao.getAllHistory()
    val fireBaseListNotInHistoryList= mutableListOf<HistoryData>()
    for (i in fireBaseHistoryData){
        if ( historyDataList.find {it.id == i.id}==null){
            fireBaseListNotInHistoryList.add(i)
        }
        else{
            historyDataList.minus(i)
        }
    }

    if(historyDataList.find { it.isSynced } !=null){
        if(fireBaseListNotInHistoryList.isNotEmpty()){
            FireStoreClass().deleteHistoryDataFromFireStore(fireBaseListNotInHistoryList)
        }
    }
    else{
        if(fireBaseListNotInHistoryList.isNotEmpty()){
            for (historyData in fireBaseListNotInHistoryList){
                databaseDao.insertInHistory(historyData)

            }


        }
    }
    if(historyDataList.isNotEmpty()){
        FireStoreClass().addCustomerHistoryDataToFireStore(historyDataList)

        for(i in historyDataList){
            databaseDao.updateHistory(i.copy(isSynced = true))
        }
    }
}


private fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network= connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

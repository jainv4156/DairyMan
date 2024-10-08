package com.example.dairyman.firebase

import com.example.dairyman.data.model.DairyData
import com.example.dairyman.data.model.HistoryData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FireStoreClass {
    private val mFireStoreDb = FirebaseFirestore.getInstance()

    fun addCustomerDairyDataToFireStore(

        customersInfo: List<DairyData>
    ){
         val batch=mFireStoreDb.batch()

        for(i in customersInfo){
            batch.set(mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").document(i.id),i.copy(isSynced = true))
        }
        batch.commit()
    }
    fun deleteDairyDataFromFireStore(dairyDataList:List<DairyData>){
        val batch = mFireStoreDb.batch()
        for(dairyData in dairyDataList){
            batch.delete(mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").document(dairyData.id))
        }
        batch.commit()
    }

    fun updateDiaryDataToFireStore(diaryDataList: List<DairyData>) {

        val batch = mFireStoreDb.batch()
        for (diaryData in diaryDataList) {
            val docRef = mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").document(diaryData.id)
            val updates = mutableMapOf<String, Any>()
            updates["name"] = diaryData.name
            updates["rate"] = diaryData.rate
            updates["amount"] = diaryData.amount
            updates["pendingAmount"] = diaryData.pendingAmount
            updates["dateUpdated"] = diaryData.dateUpdated
            updates["isSuspended"] = diaryData.isSuspended
            batch.update(docRef, updates)
        }
        batch.commit()
    }

    private fun getCurrentUserMail(): String {
        return FirebaseAuth.getInstance().currentUser?.email.toString()
    }

    suspend fun getCustomerDairyDataFromFireStore():MutableList<DairyData> {
        val data = mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").get()
             .await()
        val list:MutableList<DairyData> = mutableListOf()
        for(i in data.documents){
            list.add(i.toObject(DairyData::class.java)!!)
        }
        return list
    }



    fun addCustomerHistoryDataToFireStore(
        customersHistory: List<HistoryData>
    ){
        val batch=mFireStoreDb.batch()

        for(i in customersHistory){
            batch.set(mFireStoreDb.collection("CustomerHistory").document(getCurrentUserMail()).collection("data").document(i.id),i.copy(isSynced = true))
        }
        batch.commit()
    }

     suspend fun getCustomerHistoryDataFromFireStore():MutableList<HistoryData> {
        val data= mFireStoreDb.collection("CustomerHistory").document(getCurrentUserMail()).collection("data").get().await()
         val list:MutableList<HistoryData> = mutableListOf()
         for(i in data.documents){
             list.add(i.toObject(HistoryData::class.java)!!)
         }
        return list
    }
    fun deleteHistoryDataFromFireStore(fireBaseListNotInHistoryList: List<HistoryData>) {
        val batch = mFireStoreDb.batch()
        for(historyData in fireBaseListNotInHistoryList){
            batch.delete(mFireStoreDb.collection("CustomerHistory").document(getCurrentUserMail()).collection("data").document(historyData.id))
        }
        batch.commit()
    }

}
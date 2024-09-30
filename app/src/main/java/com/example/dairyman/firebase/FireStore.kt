package com.example.dairyman.firebase

import android.util.Log
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.Data.Model.HistoryData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreClass {
    private val mFireStoreDb = FirebaseFirestore.getInstance()

    fun addCustomerDairyDataToFireStore(

        customersInfo: MutableList<DairyData>
    ){
         val batch=mFireStoreDb.batch()

        for(i in customersInfo){
            batch.set(mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").document(i.id.toString()),i)
        }
        batch.commit().addOnSuccessListener {
            Log.d("User","Sucess")
        }
    }

    fun updateDiaryDataToFireStore(diaryDataList: List<DairyData>) {

        val batch = mFireStoreDb.batch()
        for (diaryData in diaryDataList) {
            val docRef = mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").document(diaryData.id.toString())
            val updates = mutableMapOf<String, Any>()
            updates["name"] = diaryData.name
            updates["rate"] = diaryData.rate
            updates["amount"] = diaryData.amount
            updates["pendingAmount"] = diaryData.pendingAmount
            updates["dateUpdated"] = diaryData.dateUpdated
            batch.update(docRef, updates)
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d("Firestore", "Diary data updated successfully")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating diary data", e)
            }
    }

    private fun getCurrentUserMail(): String {
        return FirebaseAuth.getInstance().currentUser?.email.toString()
    }

    fun getCustomerDairyDataFromFireStore():MutableList<DairyData> {
        val data: MutableList<DairyData> = mutableListOf()
         mFireStoreDb.collection("CustomerData").document(getCurrentUserMail()).collection("data").get()
        .addOnSuccessListener {
            for(i in it.documents){
                data.add(i.toObject(DairyData::class.java)!!)
            }
        }
        return data
    }



    fun addCustomerHistoryDataToFireStore(
        customersHistory: List<HistoryData>
    ){
        val batch=mFireStoreDb.batch()

        for(i in customersHistory){
            batch.set(mFireStoreDb.collection("CustomerHistory").document(getCurrentUserMail()).collection("data").document(i.id.toString()),i)
        }
        batch.commit().addOnSuccessListener {
            Log.d("User","Sucess")
        }
    }



    fun getCustomerHistoryDataFromFireStore():MutableList<HistoryData> {
        val data: MutableList<HistoryData> = mutableListOf()
        mFireStoreDb.collection("CustomerHistory").document(getCurrentUserMail()).collection("data").get()
            .addOnSuccessListener {
                for(i in it.documents){
                    data.add(i.toObject(HistoryData::class.java)!!)
                }
            }
        return data
    }
}
package com.example.dairyman.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HistoryViewModel: ViewModel() {
    private val isChangeAmountViewActive=mutableStateOf(false)
    private val pendingAmount= mutableStateOf("")
    private val databaseDao= DairyApp.db.DatabaseDao()
    private var pendingAmountId= ""



    fun setPendingAmountId(id:String){
        pendingAmountId=id
    }

    fun getPendingAmount():String{
        return pendingAmount.value
    }
    fun setPendingAmount(amount:String){
        pendingAmount.value=amount
    }
    fun getChangeAmountViewStatus():Boolean{
        return isChangeAmountViewActive.value
    }
    fun setChangeAmountViewStatus(status:Boolean){
        isChangeAmountViewActive.value=status

    }

     suspend fun updatePendingAmount(){
        viewModelScope.launch (IO){
            val dairyData=databaseDao.getDairyDataById(pendingAmountId).first()
            databaseDao.upsertDairyData(dairyData.copy(pendingAmount = pendingAmount.value.toInt()))
            setChangeAmountViewStatus(false)
            setPendingAmount("")
        }
    }
}
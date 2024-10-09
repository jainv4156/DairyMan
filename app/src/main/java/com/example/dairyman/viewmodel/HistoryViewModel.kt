package com.example.dairyman.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.data.model.HistoryData
import com.example.dairyman.snackBar.SnackBarAction
import com.example.dairyman.snackBar.SnackBarController
import com.example.dairyman.snackBar.SnackBarEvent
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

    suspend fun addInPendingAmount(){
        val dairyData=databaseDao.getDairyDataById(pendingAmountId).first()
        val currentPendingAmount=dairyData.pendingAmount
        val newPendingAmount=currentPendingAmount+pendingAmount.value.toInt()
        val newDairyData=dairyData.copy(pendingAmount = newPendingAmount)
        updatePendingAmount(newDairyData)
        databaseDao.insertInHistory(HistoryData(dataId = dairyData.id, balanceChange = +pendingAmount.value.toInt()))


    }

     suspend fun subtractFromPendingAmount(){
         val dairyData=databaseDao.getDairyDataById(pendingAmountId).first()
         val currentPendingAmount=dairyData.pendingAmount
         val newPendingAmount=currentPendingAmount-pendingAmount.value.toInt()
         val newDairyData=dairyData.copy(pendingAmount = newPendingAmount)
         updatePendingAmount(newDairyData)
         databaseDao.insertInHistory(HistoryData(dataId = dairyData.id, balanceChange = -pendingAmount.value.toInt()))

    }
    private suspend fun updatePendingAmount(newDairyData: DairyData) {
        viewModelScope.launch (IO){
            try{
                databaseDao.upsertDairyData(newDairyData)
                setChangeAmountViewStatus(false)
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Amount is Updated to ${newDairyData.pendingAmount}",
                        action = SnackBarAction(name = "X")
                    )
                )
                setPendingAmount("")

            }catch (e:Exception){
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Something wrong happened",
                        action = SnackBarAction(name = "X")
                    )
                )
            }
        }
    }
}
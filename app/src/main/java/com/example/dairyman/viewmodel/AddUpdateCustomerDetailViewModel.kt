package com.example.dairyman.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.Data.Model.AddUpdateCustomerDetailData.AddUpdateCustomerDetailModel
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.SnackBar.SnackBarController
import com.example.dairyman.SnackBar.SnackBarEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddUpdateCustomerDetailViewModel: ViewModel() {
    private var _addUpdateCustomerDetailData by mutableStateOf(AddUpdateCustomerDetailModel(name = "", rate = "", amount = "", pendingAmount = "0"))
    private val databaseDao= DairyApp.db.DatabaseDao()
    fun fetchAddUpdateCustomerDetailData():AddUpdateCustomerDetailModel{
        return _addUpdateCustomerDetailData
    }
    fun setAddUpdateCustomerDetailData(newAddUpdateCustomerDetailModel: AddUpdateCustomerDetailModel){
        _addUpdateCustomerDetailData=newAddUpdateCustomerDetailModel
    }

    fun addUpdateDairyData(id:Long){
        lateinit var dairyData: DairyData
        val isNew :Boolean
        if(id==0L){
            dairyData= DairyData(
                name = _addUpdateCustomerDetailData.name,
                rate = _addUpdateCustomerDetailData.rate.toFloat().toInt(),
                amount = _addUpdateCustomerDetailData.amount.toFloat(),
                pendingAmount = _addUpdateCustomerDetailData.pendingAmount.toInt(),
                tempAmount = _addUpdateCustomerDetailData.amount.toFloat(),
            )
            isNew=true
        }
        else{
            dairyData= DairyData(
                id = id,
                name = _addUpdateCustomerDetailData.name,
                rate = _addUpdateCustomerDetailData.rate.toInt(),
                amount = _addUpdateCustomerDetailData.amount.toFloat(),
                pendingAmount = _addUpdateCustomerDetailData.pendingAmount.toInt(),
                tempAmount = _addUpdateCustomerDetailData.amount.toFloat(),
                dateUpdated = System.currentTimeMillis()
            )
            isNew=false
        }

        try {
            viewModelScope.launch(IO){
                databaseDao.upsertDairyData(dairyData)
            }

            viewModelScope.launch {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message =
                        if(isNew){
                            dairyData.name+" Added Successfully"
                        }else "Customer Record Updated Successfully"

                    )
                )
            }


        }catch (e:Exception){
            viewModelScope.launch {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Something Went Wrong"
                    )
                )
            }
        }



    }
    fun preFillUpdateInput(id: Long){
        viewModelScope.launch {
            getDairyDataById(id).collect { dairyData ->
                dairyData.let {
                   setAddUpdateCustomerDetailData(newAddUpdateCustomerDetailModel = AddUpdateCustomerDetailModel(
                       name =it.name,
                       rate = it.rate.toString(),
                       amount = it.amount.toString(),
                       pendingAmount = it.pendingAmount.toString()
                   ))
                }
            }
        }
    }
    private fun getDairyDataById(id:Long): Flow<DairyData> {
        return databaseDao.getDairyDataById(id)
    }
}
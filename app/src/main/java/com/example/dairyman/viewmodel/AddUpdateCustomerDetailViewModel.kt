package com.example.dairyman.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.data.model.addUpdateCustomerDetailData.AddUpdateCustomerDetailModel
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.snackBar.SnackBarAction
import com.example.dairyman.snackBar.SnackBarController
import com.example.dairyman.snackBar.SnackBarEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    fun addUpdateDairyData(id:String){
        lateinit var dairyData: DairyData
        val isNew :Boolean
        if(id==""){
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

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message =
                        if(isNew){
                            dairyData.name+" Added Successfully"
                        }else "Customer Record Updated Successfully",
                        action = SnackBarAction(
                            name = "X"
                        )

                    )
                )
            }


        }catch (e:Exception){
            viewModelScope.launch {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Something Went Wrong",
                        action = SnackBarAction(
                            name = "X"
                        )
                    )
                )
            }
        }
    }
    fun preFillUpdateInput(id: String){
        viewModelScope.launch{
            val dairyData=getDairyDataById(id).first()
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
    private fun getDairyDataById(id:String): Flow<DairyData> {
        return databaseDao.getDairyDataById(id)
    }
}
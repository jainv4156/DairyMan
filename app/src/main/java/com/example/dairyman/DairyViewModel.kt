package com.example.dairyman

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dairyman.Data.DairyData
import com.example.dairyman.Data.HistoryData
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DairyViewModel:ViewModel(){

    private var  name = mutableStateOf("")
    private var rate = mutableStateOf("")
    private var amount = mutableStateOf("")
    private var pendingAmount = mutableStateOf("")
    private var dairyDataFromAmountPressed: MutableState<DairyData> = mutableStateOf(DairyData(0,"",0,0F,0,0F))
    private var dayForTempAmount = mutableStateOf("1")
    fun getDayForTempAmount(): String {
        return dayForTempAmount.value
    }
    fun setDayForTempAmount(value:String){
        dayForTempAmount.value= value
    }
    fun setDairyDataFromAmountPressed(dairyData: DairyData){
        dairyDataFromAmountPressed.value=dairyData
    }
    fun getDairyDataFromAmountPressed(): DairyData {
        return dairyDataFromAmountPressed.value
    }

    lateinit var getAllDairyData: Flow<List<DairyData>>
    fun getDairyDataById(id:Long):Flow<DairyData>{
        return dairyDao.getDairyDataById(id)
    }

    private val dairyDao=DairyApp.db.dairyDao()

    fun getName():String{
        return name.value
    }


    init {
            viewModelScope.launch (IO){
                getAllDairyData= dairyDao.loadAllDairyData()
            }
    }


    fun setName(newString:String){
        name.value=newString
    }
    fun getRate():String{
        return rate.value
    }
    fun getAmount():String{
        return amount.value
    }

    fun getPendingAmount():String{
        return pendingAmount.value
    }
    fun setRate(string:String){
        rate.value=string
    }
    fun setAmount(string:String){
        amount.value=string
    }
    fun setPendingAmount(string:String){
        pendingAmount.value=string
    }


    fun addUpdateDairyData(userData: DairyData){
        viewModelScope.launch(IO){
            dairyDao.upsertDairyData(userData)
        }

    }



    fun updateTodayAmountButton(){
        val historyDataList: MutableList<HistoryData> = mutableListOf()
        viewModelScope.launch{
            val dataToRecord= dairyDao.getDataToRecord()
            dataToRecord.forEach{
                if(it.dayForTempAmount!=1){
                    dairyDao.updateDairyData(it.copy(dayForTempAmount = it.dayForTempAmount-1))
                }
                else{
                    dairyDao.updateDairyData(it.copy(dayForTempAmount = 0,tempAmount = it.amount))
                }
                val child=HistoryData(amount = it.amount, tempAmount = it.tempAmount, date = System.currentTimeMillis(), dataId = it.id)
                historyDataList+=child
            }
            dairyDao.insertHistory(historyDataList)
            dairyDao.updateTodayAmount()
        }
    }

    fun deleteDataById(dairyData: DairyData){
        viewModelScope.launch ( IO ){
            dairyDao.deleteDairyData(dairyData)
        }
    }
}
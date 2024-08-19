package com.example.dairyman

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.Data.DairyData
import com.example.dairyman.Data.HistoryData
import com.example.dairyman.Data.JoinedResult
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DairyViewModel:ViewModel(){

    private var  name = mutableStateOf("")
    private var rate = mutableStateOf("")
    private var amount = mutableStateOf("")
    private var pendingAmount = mutableStateOf("0")
    private var dairyDataFromAmountPressed: MutableState<DairyData> = mutableStateOf(DairyData(0,"",0,0F,0,0F))
    private var dayForTempAmount = mutableStateOf("1")
    private val isAlertDialogBox= mutableStateOf(false)

    fun getIsAlertDialogBox(): MutableState<Boolean> {
        return isAlertDialogBox
    }
    fun setAlertDialogBox(value:Boolean){
        isAlertDialogBox.value=value

    }
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

    private val dairyDao= DairyApp.db.dairyDao()

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

     fun checkTodayUpdate() {
        val todayDate= SimpleDateFormat("yyyy/mm/dd", Locale.getDefault()).format(System.currentTimeMillis())

         viewModelScope.launch (IO){
            if(dairyDao.getHistoryCount(todayDate)==0){
                updateTodayAmountButton()
            }
             else{
                setAlertDialogBox(true)
            }

        }

    }
    fun updateTodayAmountButton(){
        val historyDataList: MutableList<HistoryData> = mutableListOf()
        viewModelScope.launch{
            val dataToRecord= dairyDao.loadAllDairyData().first()
            dataToRecord.forEach{
                if(it.dayForTempAmount!=1){
                    dairyDao.updateDairyData(it.copy(dayForTempAmount = it.dayForTempAmount-1))
                }
                else{
                    dairyDao.updateDairyData(it.copy(dayForTempAmount = 0,tempAmount = it.amount))
                }
                val child= HistoryData(
                    amount = it.amount, tempAmount = it.tempAmount, date = SimpleDateFormat("yyyy/mm/dd", Locale.getDefault()).format(System.currentTimeMillis())
                    , dataId = it.id)
                historyDataList+=child
            }
            dairyDao.insertHistory(historyDataList)
            dairyDao.updateTodayAmount()
        }
    }

    fun deleteDataById(dairyData: DairyData){
        viewModelScope.launch ( IO ){
            dairyDao    .deleteHistoryData(dairyData.id)
            dairyDao.deleteDairyData(dairyData)
        }
    }
    fun getHistoryById(id:Long):Flow<List<JoinedResult>>{
        return dairyDao.getHistoryById(id = id)
    }

    fun getProfileDataForHistory(id: Long):Flow<DairyData>{
        return dairyDao.getDairyDataById(id=id)
    }
}
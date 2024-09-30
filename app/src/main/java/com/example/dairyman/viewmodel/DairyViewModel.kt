package com.example.dairyman.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.Data.DatabaseDao
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.Data.Model.HistoryData
import com.example.dairyman.Data.Model.JoinedResult
import com.example.dairyman.firebase.FireStoreClass
import com.google.api.ResourceDescriptor.History
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Locale

class DairyViewModel:ViewModel(){
    private var dayForTempAmount = mutableStateOf("1")
    private var tempAmount= mutableStateOf("0")
    private var idTempAmount= 0L
    private val isAlertDialogBox= mutableStateOf(false)
    lateinit var getAllDairyData: Flow<List<DairyData>>
    private val databaseDao= DairyApp.db.DatabaseDao()
    private val isSetTempAmountViewActive= mutableStateOf(false)
    private val isBlurredBackgroundActive= mutableStateOf(false)
    private val isEditDeleteButtonEnabled= mutableLongStateOf(-1L)

    private fun setIsEditDeleteButtonEnabled(value:Long){
        isEditDeleteButtonEnabled.longValue=value
    }
    fun getIsEEditDeleteButtonEnabled():Long{
        return isEditDeleteButtonEnabled.longValue
    }

    private fun setIsBlurredBackgroundActive(value:Boolean){
        isBlurredBackgroundActive.value=value
    }
    fun getIsBlurredBackgroundActive():Boolean{
        return isBlurredBackgroundActive.value
    }
    fun setIdTempAmount(id:Long){
        idTempAmount=id
    }
    fun getIsSetTempAmountViewActive():Boolean{
        return isSetTempAmountViewActive.value
    }
    fun setIsSetTempAmountViewActive(value:Boolean){
        isSetTempAmountViewActive.value=value
    }
    fun getTempAmount(): String {
        return tempAmount.value
    }
    fun setTempAmount(value:String){
        tempAmount.value= value
    }

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

    init {
            viewModelScope.launch (IO){
                getAllDairyData= databaseDao.loadAllDairyData()
            }
    }

     fun checkTodayUpdate() {
        val todayDate= SimpleDateFormat("yyyy/mm/dd", Locale.getDefault()).format(System.currentTimeMillis())

         viewModelScope.launch (IO){
            if(databaseDao.getHistoryCount(todayDate)==0){
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
            val dataToRecord= databaseDao.loadAllDairyData().first()
            dataToRecord.forEach{
                if(it.dayForTempAmount>=1){
                    databaseDao.upsertDairyData(it.copy(dayForTempAmount = it.dayForTempAmount-1))
                }
                else{
                    databaseDao.upsertDairyData(it.copy(dayForTempAmount = 0,tempAmount = it.amount))
                }
                val child= HistoryData(
                    amount = it.amount, tempAmount = it.tempAmount, date = SimpleDateFormat("yyyy/mm/dd", Locale.getDefault()).format(System.currentTimeMillis())
                    , dataId = it.id)
                historyDataList+=child
            }
            databaseDao.insertHistory(historyDataList)
            databaseDao.updateTodayAmount()
        }
    }
    fun deleteDataById(dairyData: DairyData){
        viewModelScope.launch(IO){
            databaseDao.deleteHistoryData(dairyData.id)
            databaseDao.deleteDairyData(dairyData)
        }
    }
    fun getHistoryById(id:Long):Flow<List<JoinedResult>>{
        return databaseDao.getHistoryById(id = id)
    }
    fun getProfileDataForHistory(id: Long):Flow<DairyData>{
        return databaseDao.getDairyDataById(id=id)
    }

    private fun preFillSetTempAmountView(dairyData: DairyData){
        setTempAmount(dairyData.tempAmount.toString())
        setDayForTempAmount(dairyData.dayForTempAmount.toString())
    }

    fun readySetTempAmountView(){
        viewModelScope.launch {
            databaseDao.getDairyDataById(idTempAmount).first().let {
                preFillSetTempAmountView(it)
            }
        }
        setIsSetTempAmountViewActive(true)
        setIsBlurredBackgroundActive(true)
    }

    fun updateTempAmountView() {
        viewModelScope.launch {
            databaseDao.upsertDairyData(databaseDao.getDairyDataById(idTempAmount).first().copy(tempAmount = tempAmount.value.toFloat(), dayForTempAmount =dayForTempAmount.value.toInt()))
        }
        setIsSetTempAmountViewActive(false)
    }

    fun selectMoreOption(id: Long) {
        setIsEditDeleteButtonEnabled(id)
        setIsBlurredBackgroundActive(true)
    }

    fun deselectMoreOption(){
        setIsEditDeleteButtonEnabled(-1L)
        setIsBlurredBackgroundActive(false)
    }
    suspend fun syncDataWithCloud() {
        syncDairyDataWithCloud()
        syncHistoryDataWithCloud()

    }

    private suspend fun syncDairyDataWithCloud(){
        val fireBaseDairyData:List<DairyData> = FireStoreClass().getCustomerDairyDataFromFireStore()
        val dairyDataList:List<DairyData> = runBlocking { getAllDairyData.first() }
        val listToBeAddInDairyData= mutableListOf<DairyData>()
        val listToUpdateFireBaseDairyData= mutableListOf<DairyData>()
        for (i in fireBaseDairyData){
            if ( dairyDataList.find {it.id ==i.id}==null){
                listToBeAddInDairyData.add(i)
            }
            else{
                val currentDairyData=dairyDataList.find { it.id==i.id }
                if(currentDairyData!=i){
                    listToUpdateFireBaseDairyData.add(currentDairyData!!)
                }
                dairyDataList.minus(currentDairyData)
            }
        }
        viewModelScope.launch(IO){
            for( i in dairyDataList) {
                databaseDao.upsertDairyData(i)
            }
        }
        viewModelScope.launch (IO){
            if(listToBeAddInDairyData.isNotEmpty()){
                FireStoreClass().addCustomerDairyDataToFireStore(listToBeAddInDairyData)
            }
            if(listToUpdateFireBaseDairyData.isNotEmpty()) {
                FireStoreClass().updateDiaryDataToFireStore(listToUpdateFireBaseDairyData)
            }
        }
    }
    private suspend fun syncHistoryDataWithCloud()
    {
        val fireBaseHistoryData:List<HistoryData> = FireStoreClass().getCustomerHistoryDataFromFireStore()
        val historyDataList:List<HistoryData> =databaseDao.getAllHistory()
        val listToBeAddInHistoryData= mutableListOf<HistoryData>()
        for (i in fireBaseHistoryData){
            if ( historyDataList.find {it.id ==i.id}==null){
                listToBeAddInHistoryData.add(i)
            }
            else{
                historyDataList.minus(i)
            }
        }
        viewModelScope.launch(IO){
                databaseDao.insertHistory(historyDataList)
        }
        viewModelScope.launch (IO){
            if(listToBeAddInHistoryData.isNotEmpty()){
                FireStoreClass().addCustomerHistoryDataToFireStore(listToBeAddInHistoryData)
            }
        }

    }


}
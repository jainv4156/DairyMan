package com.example.dairyman.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.Data.Model.HistoryData
import com.example.dairyman.Data.Model.JoinedResult
import com.example.dairyman.firebase.FireStoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DairyViewModel:ViewModel(){
    private var dayForTempAmount = mutableStateOf("1")
    private var tempAmount= mutableStateOf("0")
    private var idTempAmount= 0L
    private val isAlertDialogBox= mutableStateOf(false)
    private  var customersList: MutableStateFlow<List<DairyData>> = MutableStateFlow(listOf())
    private lateinit var customerListFromDatabase: Flow<List<DairyData>>
    private val databaseDao= DairyApp.db.DatabaseDao()
    private val isSetTempAmountViewActive= mutableStateOf(false)
    private val isBlurredBackgroundActive= mutableStateOf(false)
    private val isEditDeleteButtonEnabled= mutableLongStateOf(-1L)
    private var searchQuery =  mutableStateOf("")
    private val signInAlertBox= mutableStateOf(false)
    private val isSearchActive= mutableStateOf(false)
    val isActionButtonExtended = mutableStateOf(false)

    fun getIsSearchActive():Boolean{
        return isSearchActive.value
    }
    fun enableSearch(){
        isSearchActive.value=true
    }
    fun disableSearch() {
        isSearchActive.value = false
    }

    fun getSearchQuery(): String{
        return searchQuery.value
    }
    fun setSearchQuery(value:String){
        searchQuery.value=value
    }
    fun getIsActionButtonExtended():MutableState<Boolean>{
        return isActionButtonExtended

    }
    fun setIsActionButtonExtended(value:Boolean){
        isActionButtonExtended.value=value
    }


    fun getSignInAlertBox():MutableState<Boolean>{
        return signInAlertBox
    }
    fun setSignInAlertBox(value:Boolean){
        signInAlertBox.value=value
    }

    private fun setIsEditDeleteButtonEnabled(value:Long){
        isEditDeleteButtonEnabled.longValue=value
    }
    fun getIsEEditDeleteButtonEnabled():Long{
        return isEditDeleteButtonEnabled.longValue
    }

    private fun setIsBlurredBackgroundActive(value:Boolean){
        isBlurredBackgroundActive.value=value
    }

    fun setIdTempAmount(id:Long){
        idTempAmount=id
    }
    fun getIsSetTempAmountViewActive():Boolean{
        return isSetTempAmountViewActive.value
    }
    private fun setIsSetTempAmountViewActive(value:Boolean){
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
                customerListFromDatabase = databaseDao.loadAllDairyData()
                databaseDao.loadAllDairyData().collect{list->
                customersList.value=list}
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
                    amount = it.amount, tempAmount = it.tempAmount, date = System.currentTimeMillis()
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
    }
    fun disableSetTempAmountView(){
        setIsSetTempAmountViewActive(false)
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
        if(FirebaseAuth.getInstance().currentUser!=null){
            syncDairyDataWithCloud()
            syncHistoryDataWithCloud()
            setIsActionButtonExtended(false)
        }
        else{
            signInAlertBox.value=true
        }
    }

    private suspend fun syncDairyDataWithCloud(){
        val fireBaseDairyData:List<DairyData> =  FireStoreClass().getCustomerDairyDataFromFireStore()
        val dairyDataList:List<DairyData> = customersList.first()
        val listToBeAddInDairyData= mutableListOf<DairyData>()
        val listToUpdateFireBaseDairyData= mutableListOf<DairyData>()
//        val listToResolveCollision= mutableListOf<DairyData>()

        for (i in fireBaseDairyData){

            if ( dairyDataList.find {it.id ==i.id}==null){
                listToBeAddInDairyData.add(i)
            }
            else{
                val currentDairyData=dairyDataList.find { it.id==i.id }
//                if(currentDairyData!!.isSynced){
                    if(currentDairyData!=i){
                        listToUpdateFireBaseDairyData.add(currentDairyData!!)
                    }
//                }
//                else{
//                    listToResolveCollision.add(currentDairyData)
//                }
                dairyDataList.minus(currentDairyData)

            }
            Log.d("2",listToBeAddInDairyData.toString())
        }
        Log.d("3",listToBeAddInDairyData.toString())
        viewModelScope.launch(IO){
            for( i in listToBeAddInDairyData) {
                databaseDao.upsertDairyData(i)
                Log.d("3",listToBeAddInDairyData.toString())

            }
            for(i in dairyDataList){
                databaseDao.upsertDairyData(i.copy(isSynced = true))
            }
        }
        viewModelScope.launch (IO){
            if(dairyDataList.isNotEmpty()){
                FireStoreClass().addCustomerDairyDataToFireStore(dairyDataList)
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
                databaseDao.insertHistory(listToBeAddInHistoryData)
            for(i in historyDataList){
                databaseDao.updateHistory(i.copy(isSynced = true))
            }
        }
        viewModelScope.launch (IO){
            if(historyDataList.isNotEmpty()){
                FireStoreClass().addCustomerHistoryDataToFireStore(historyDataList)
            }
        }
    }

    fun getCustomersList(): Flow<List<DairyData>> {
        return customersList
    }

    suspend fun getSearchFilteredList(){
          customersList.value = customerListFromDatabase.first().filter {item->
              item.name.contains(searchQuery.value,ignoreCase = false)
          }
    }


}
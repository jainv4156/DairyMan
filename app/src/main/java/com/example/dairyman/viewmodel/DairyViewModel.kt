package com.example.dairyman.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.Data.Model.HistoryData
import com.example.dairyman.SnackBar.SnackBarController
import com.example.dairyman.SnackBar.SnackBarEvent
import com.example.dairyman.firebase.FireStoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale

class DairyViewModel:ViewModel(){
    private var dayForTempAmount = mutableStateOf("1")
    private var tempAmount= mutableStateOf("0")
    private var idTempAmount= 0L
    private  var customersList: MutableStateFlow<List<DairyData>> = MutableStateFlow(listOf())
    private lateinit var customerListFromDatabase: Flow<List<DairyData>>
    private val databaseDao= DairyApp.db.DatabaseDao()

    private var searchQuery =  mutableStateOf("")
    private val mIsSearchActive= mutableStateOf(false)
    private val mIsAlertDialogBox= mutableStateOf(false)
    private val mIsSetTempAmountViewActive= mutableStateOf(false)
    private val mIsBlurredBackgroundActive= mutableStateOf(false)
    private val mIsEditDeleteButtonEnabled= mutableLongStateOf(-1L)
    private val mSignInAlertBox= mutableStateOf(false)
    val isActionButtonExtended = mutableStateOf(false)
    private val mIsFloatingButtonVisible= mutableStateOf(true)
    private val mIsDeleteAlertEnabled= mutableStateOf(DairyData())
    private var mAlertDialogTitle=""
    private var mIsUpdateAmountAlertEnable= mutableStateOf(false)

    fun getIsUpdateAmountAlertEnable():Boolean{
        return mIsUpdateAmountAlertEnable.value
    }

    fun getAlertDialogTitle():String{
        return mAlertDialogTitle
    }
    fun getIsDeleteAlertEnabled():DairyData{
        return mIsDeleteAlertEnabled.value
    }
    fun getIsFloatingButtonVisible():Boolean{
        return mIsFloatingButtonVisible.value
    }
    fun getSearchQuery(): String{
        return searchQuery.value
    }
    fun getIsSearchActive():Boolean{
        return mIsSearchActive.value
    }
    fun getTempAmount(): String {
        return tempAmount.value
    }
    fun getIsSetTempAmountViewActive():Boolean{
        return mIsSetTempAmountViewActive.value
    }
    fun getIsAlertDialogBox(): MutableState<Boolean> {
        return mIsAlertDialogBox
    }
    fun getDayForTempAmount(): String {
        return dayForTempAmount.value
    }
    fun getIsActionButtonExtended():Boolean{
        return isActionButtonExtended.value
    }
    fun getIsEEditDeleteButtonEnabled():Long{
        return mIsEditDeleteButtonEnabled.longValue
    }



    private fun setIsBlurredBackgroundActive(value:Boolean){
        mIsBlurredBackgroundActive.value=value
    }
    private fun setIsEditDeleteButtonEnabled(value:Long){
        mIsEditDeleteButtonEnabled.longValue=value
    }
    private fun setAlertDialogBox(value:Boolean){
        mIsAlertDialogBox.value=value
    }
    private fun mSetIsFloatingButtonVisible(value:Boolean){
        mIsFloatingButtonVisible.value=value
    }
    private fun setIsSetTempAmountViewActive(value:Boolean){
        mIsSetTempAmountViewActive.value=value
    }
    fun setIsActionButtonExtended(value:Boolean){
        isActionButtonExtended.value=value
    }

    private fun enableAlertDialogBax(){
        setIsActionButtonExtended(false)
        mSetIsFloatingButtonVisible(false)
        mIsAlertDialogBox.value=true
    }
    fun enableSearch(){
        mSetIsFloatingButtonVisible(false)
        mIsSearchActive.value=true
    }
    private fun enableUpdateAmountAlert(){
        mAlertDialogTitle= "You Have Added Today's Amount Do You Wish To Continue adding the Amount"
        mIsUpdateAmountAlertEnable.value=true
        enableAlertDialogBax()

    }
    fun enableDeleteAlert(item: DairyData) {
        resetHomeViewState()
        mAlertDialogTitle="Do you Want To Delete This Customer Account . You May Not Be Able To Recover It Again"
        mIsDeleteAlertEnabled.value=item
        enableAlertDialogBax()
    }

    fun resetHomeViewState(){
        setSearchQuery("")
        setIsActionButtonExtended(false)
        setAlertDialogBox(false)
        mSetIsFloatingButtonVisible(true )
        setIsEditDeleteButtonEnabled(-1L)
        setIsBlurredBackgroundActive(false)
        setIsSetTempAmountViewActive(false)
        mIsSearchActive.value = false
        setSignInAlertBox(false)
        mIsDeleteAlertEnabled.value=DairyData(id=0L)
        mAlertDialogTitle=""
        mIsUpdateAmountAlertEnable.value=false


    }

    init {
            viewModelScope.launch (IO){
                customerListFromDatabase = databaseDao.loadAllDairyData()
                databaseDao.loadAllDairyData().collect{list->
                customersList.value=list}
            }
    }


    fun setIdTempAmount(id:Long){
        idTempAmount=id
    }
    fun setSearchQuery(value:String){
        searchQuery.value=value
    }
    fun setTempAmount(value:String){
        tempAmount.value= value
    }



    fun setDayForTempAmount(value:String){
        dayForTempAmount.value= value
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getToday0001Millis(): Long {
        val today = LocalDate.now()
        val time = LocalTime.of(0, 1) // 00:01
        return today.atTime(time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
     @RequiresApi(Build.VERSION_CODES.O)
     suspend fun checkTodayUpdate() {
        val todayDate:Long=getToday0001Millis()
         viewModelScope.launch(IO){
            if(databaseDao.getHistoryCount(todayDate)==0){
                updateTodayAmountButton()
            }
             else{
                 Log.d("5","10")
                enableUpdateAmountAlert()
            } 

        }

    }

    fun updateTodayAmountButton(){
        Log.d("ha","jaja")

        try {
            val historyDataList: MutableList<HistoryData> = mutableListOf()
            Log.d("ha1","jaja")

            viewModelScope.launch{
                Log.d("h2","jaja")

                val dataToRecord= databaseDao.loadAllDairyData().first()
                dataToRecord.forEach{
                    if(it.tempAmount!=0f){

                    if(it.dayForTempAmount>=1){
                        databaseDao.upsertDairyData(it.copy(dayForTempAmount = it.dayForTempAmount-1))
                    }
                    else{
                        databaseDao.upsertDairyData(it.copy(dayForTempAmount = 0,tempAmount = it.amount))
                    }
                    val child= HistoryData(
                        amount = it.tempAmount, rate = it.rate, date = System.currentTimeMillis()
                        , dataId = it.id)
                    historyDataList+=child
                    }

                }
                Log.d("h3","jaja")

                databaseDao.insertHistory(historyDataList)
                databaseDao.updateTodayAmount()
            }

            viewModelScope.launch {
                Log.d("ha4","jaja")

                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message =
                        "Today Amount Updated"
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
    fun deleteDataById(dairyData: DairyData=mIsDeleteAlertEnabled.value){
        Log.d("ju","jm")
        try {
            viewModelScope.launch(IO){
                databaseDao.deleteHistoryData(dairyData.id)
                databaseDao.deleteDairyData(dairyData)
            }
            viewModelScope.launch {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Customer Record Deleted Successfully"

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
    fun getHistoryById(id:Long):Flow<List<HistoryData>>{
        return databaseDao.getHistoryById(id = id)
    }
    fun getProfileDataForHistory(id: Long):Flow<DairyData>{
        return databaseDao.getDairyDataById(id=id)
    }

    private fun preFillSetTempAmountView(dairyData: DairyData){
        setTempAmount(dairyData.tempAmount.toString())
        if(dairyData.dayForTempAmount.toString() != "0"){
            setDayForTempAmount(dairyData.dayForTempAmount.toString())
        }
    }

    fun readySetTempAmountView(){
        viewModelScope.launch {
            databaseDao.getDairyDataById(idTempAmount).first().let {
                preFillSetTempAmountView(it)
            }
        }
        mSetIsFloatingButtonVisible(false)
        setIsSetTempAmountViewActive(true)
    }

    fun updateTempAmount() {
        try {
            viewModelScope.launch {
                databaseDao.upsertDairyData(databaseDao.getDairyDataById(idTempAmount).first().copy(tempAmount = tempAmount.value.toFloat(), dayForTempAmount =dayForTempAmount.value.toInt()))
            }
                viewModelScope.launch {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Amount is ${tempAmount.value} for "+ if(dayForTempAmount.value=="1") "1 day" else " ${dayForTempAmount.value} Days"
                        ))
                }

        }
        catch (e:Exception){
            viewModelScope.launch {
                SnackBarController.sendEvent(
                  event = SnackBarEvent(
                      message = "Something Went Wrong"
                ))
            }
        }
        
        resetHomeViewState()
    }
    fun getSignInAlertBox():Boolean{
        return mSignInAlertBox.value
    }
     private fun setSignInAlertBox(value:Boolean){
        mSignInAlertBox.value=value
    }

    fun enableMoreOption(id: Long) {
        mSetIsFloatingButtonVisible(false)
        setIsEditDeleteButtonEnabled(id)
        setIsBlurredBackgroundActive(true)
    }

    suspend fun syncDataWithCloud() {
        if(FirebaseAuth.getInstance().currentUser!=null){
            try {
                viewModelScope.launch {
                    resetHomeViewState()
                    withContext(IO){
                        syncDairyDataWithCloud()
                        syncHistoryDataWithCloud()
                    }
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Data is Synced Successfully"
                        ))
                }


            }
            catch (e:Exception){
                viewModelScope.launch {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Something Went Wrong"
                        ))
                }
            }



        }
        else{
            setSignInAlertBox(true)
            mAlertDialogTitle= "You Have To SignIn before Syncing"
            enableAlertDialogBax()
        }
    }

    private suspend fun syncDairyDataWithCloud(){
        val fireBaseDairyData:List<DairyData> =  FireStoreClass().getCustomerDairyDataFromFireStore()
        val dairyDataList:List<DairyData> = customerListFromDatabase.first()
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
        }
        viewModelScope.launch(IO){
            for( i in listToBeAddInDairyData) {
                databaseDao.upsertDairyData(i)

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
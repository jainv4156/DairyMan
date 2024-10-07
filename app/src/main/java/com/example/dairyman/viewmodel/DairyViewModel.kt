package com.example.dairyman.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dairyman.DairyApp
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.data.model.HistoryData
import com.example.dairyman.snackBar.SnackBarAction
import com.example.dairyman.snackBar.SnackBarController
import com.example.dairyman.snackBar.SnackBarEvent
import com.example.dairyman.firebase.FireStoreClass
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class DairyViewModel:ViewModel(){
    private var dayForTempAmount = mutableStateOf("1")
    private var tempAmount= mutableStateOf("0")
    private var idTempAmount= ""
    private  var customersList: MutableStateFlow<List<DairyData>> = MutableStateFlow(listOf())
    private lateinit var customerListFromDatabase: Flow<List<DairyData>>
    private val databaseDao= DairyApp.db.DatabaseDao()

    private var searchQuery =  mutableStateOf("")
    private val mIsSearchActive= mutableStateOf(false)
    private val mIsAlertDialogBox= mutableStateOf(false)
    private val mIsSetTempAmountViewActive= mutableStateOf(false)
    private val mIsBlurredBackgroundActive= mutableStateOf(false)
    private val mIsEditDeleteButtonEnabled= mutableStateOf("")
    private val mSignInAlertBox= mutableStateOf(false)
    val isActionButtonExtended = mutableStateOf(false)
    private val mIsFloatingButtonVisible= mutableStateOf(true)
    private val mIsDeleteAlertEnabled= mutableStateOf(DairyData(id=""))
    private var mAlertDialogTitle=""
    private var mIsUpdateAmountAlertEnable= mutableStateOf(false)
    private val isCircularProgressBarActive=mutableStateOf(false)


    fun getIsCircularProgressBarActive(): Boolean {
        return isCircularProgressBarActive.value
    }
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
    fun getIsEEditDeleteButtonEnabled():String{
        return mIsEditDeleteButtonEnabled.value
    }



    private fun setIsCircularProgressBarActive(value:Boolean){
        isCircularProgressBarActive.value=value
    }
    private fun setIsBlurredBackgroundActive(value:Boolean){
        mIsBlurredBackgroundActive.value=value
    }
    private fun setIsEditDeleteButtonEnabledId(value:String){
        mIsEditDeleteButtonEnabled.value=value
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
        mAlertDialogTitle="Do you Want To Delete This Customer Account Having Pending Amount ${item.pendingAmount}. You May Not Be Able To Recover It Again"

        mIsDeleteAlertEnabled.value=item
        enableAlertDialogBax()
    }

    fun resetHomeViewState(){
        setSearchQuery("")
        setIsActionButtonExtended(false)
        mIsAlertDialogBox.value=false
        mSetIsFloatingButtonVisible(true )
        setIsEditDeleteButtonEnabledId("")
        setIsBlurredBackgroundActive(false)
        setIsSetTempAmountViewActive(false)
        mIsSearchActive.value = false
        setSignInAlertBox(false)
        mIsDeleteAlertEnabled.value=DairyData(id = "")
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


    fun setIdTempAmount(id:String){
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
                enableUpdateAmountAlert()
            } 

        }

    }

    suspend fun updateTodayAmountButton(){
        viewModelScope.launch(IO){

        try {setIsCircularProgressBarActive(true)
            val historyDataList: MutableList<HistoryData> = mutableListOf()
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
            delay(500)
            for( historyData in historyDataList){

                databaseDao.insertInHistory(historyData)
            }
            databaseDao.updateTodayAmount()

            setIsCircularProgressBarActive(false)
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "Today Amount Updated",
                    action = SnackBarAction(name = "X")
                )
            )
        }catch (e:Exception){
            setIsCircularProgressBarActive(false)
            somethingWrongSnackBar()
        }
        }
    }
    suspend fun deleteDataById(){
        try {
            withContext(IO) {
                databaseDao.deleteDairyData(mIsDeleteAlertEnabled.value)
            }
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = "Customer Record Deleted Successfully",
                    action = SnackBarAction(
                        name = "X"
                    )
                )
            )
        }
        catch (e: SQLiteConstraintException) {
            withContext(Dispatchers.Main) {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = e.message.toString(),
                        action = SnackBarAction(
                            name = "X"
                        )
                    )
                )
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                somethingWrongSnackBar()
            }
        }
        resetHomeViewState()
    }
    fun getHistoryById(id:String):Flow<List<HistoryData>>{
        return databaseDao.getHistoryById(id = id)
    }
    fun getProfileDataForHistory(id: String):Flow<DairyData>{
        return databaseDao.getDairyDataById(id=id)
    }

    private fun preFillSetTempAmountView(dairyData: DairyData){
        setTempAmount(dairyData.tempAmount.toString())
        if(dairyData.dayForTempAmount.toString() != "0"){
            setDayForTempAmount(dairyData.dayForTempAmount.toString())
        }
    }

    fun readySetTempAmountView(){
        viewModelScope.launch(IO) {
            databaseDao.getDairyDataById(idTempAmount).first().let {
                preFillSetTempAmountView(it)
            }
        }
        mSetIsFloatingButtonVisible(false)
        setIsSetTempAmountViewActive(true)
    }

    fun updateTempAmount() {
        try {
            viewModelScope.launch(IO) {
                databaseDao.upsertDairyData(databaseDao.getDairyDataById(idTempAmount).first().copy(tempAmount = tempAmount.value.toFloat(), dayForTempAmount =dayForTempAmount.value.toInt()))
            }
                viewModelScope.launch {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "Amount is ${tempAmount.value} for "+ if(dayForTempAmount.value=="1") "1 day" else " ${dayForTempAmount.value} Days",
                            action = SnackBarAction(
                                name = "X"
                            )
                        ))
                }

        }
        catch (e:Exception){
            viewModelScope.launch {
                somethingWrongSnackBar()
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

    fun enableMoreOption(id: String) {
        mSetIsFloatingButtonVisible(false)
        setIsEditDeleteButtonEnabledId(id)
        setIsBlurredBackgroundActive(true)
    }


    fun getCustomersList(): Flow<List<DairyData>> {
        return customersList
    }

    suspend fun getSearchFilteredList(){
          customersList.value = customerListFromDatabase.first().filter {item->
              item.name.contains(searchQuery.value,ignoreCase = true)
          }
    }
    suspend fun checkSyncDataWithCloud(context: Context){
        if(FirebaseAuth.getInstance().currentUser!=null) {
            resetHomeViewState()
            if(isInternetAvailable(context =context )){
                setIsCircularProgressBarActive(true)
                viewModelScope.launch {
                    try {
                        syncDataWithCloud()
                        setIsCircularProgressBarActive(false)
                        SnackBarController.sendEvent(
                            event = SnackBarEvent(
                                message = "Data is Synced Successfully",
                                action = SnackBarAction(name = "X")
                            )
                        )
                    }
                    catch (e:Exception){
                        setIsCircularProgressBarActive(false)
                        somethingWrongSnackBar()
                    }
                }
            }
            else{
                viewModelScope.launch {
                    SnackBarController.sendEvent(
                        event = SnackBarEvent(
                            message = "No Internet Connection",
                            action = SnackBarAction(name = "X")
                        )
                    )
                }
            }


        }
        else{
            setSignInAlertBox(true)
            mAlertDialogTitle= "You Have To SignIn before Syncing"
            enableAlertDialogBax()
        }
    }
     suspend fun syncDataWithCloud() {
        withContext(IO){
            syncDairyDataWithCloud()
            syncHistoryDataWithCloud()
        }
    }
    private suspend fun syncDairyDataWithCloud(){
        val fireBaseDairyData:List<DairyData> =  FireStoreClass().getCustomerDairyDataFromFireStore()

        val dairyDataList:List<DairyData> = customerListFromDatabase.first()

        val fireBaseListNotInDairyData= mutableListOf<DairyData>()
        val listToUpdateFireBaseDairyData= mutableListOf<DairyData>()

        for (i in fireBaseDairyData){
            if ( dairyDataList.find {it.id ==i.id}==null){
                fireBaseListNotInDairyData.add(i)
            }
            else{
                val currentDairyData=dairyDataList.find { it.id==i.id }
                if(currentDairyData!=i){
                    listToUpdateFireBaseDairyData.add(currentDairyData!!)
                }
                dairyDataList.minus(currentDairyData)
            }
        }


            if(dairyDataList.isNotEmpty()){
                FireStoreClass().addCustomerDairyDataToFireStore(dairyDataList)
                for(dairyData in dairyDataList){
                    databaseDao.upsertDairyData(dairyData.copy(isSynced = true))
                }
            }

            if(dairyDataList.find { it.isSynced } !=null){
                if(listToUpdateFireBaseDairyData.isNotEmpty()) {
                    FireStoreClass().updateDiaryDataToFireStore(listToUpdateFireBaseDairyData)
                }
                if(fireBaseListNotInDairyData.isNotEmpty()){
                    FireStoreClass().deleteDairyDataFromFireStore(fireBaseListNotInDairyData)
                }
            }
            else{
                if(fireBaseListNotInDairyData.isNotEmpty()){
                    for( i in fireBaseListNotInDairyData) {
                        databaseDao.upsertDairyData(i)
                    }
                }
            }
    }
    private suspend fun syncHistoryDataWithCloud() {
        val fireBaseHistoryData:List<HistoryData> = FireStoreClass().getCustomerHistoryDataFromFireStore()

        val historyDataList:List<HistoryData> =databaseDao.getAllHistory()
        val fireBaseListNotInHistoryList= mutableListOf<HistoryData>()
        for (i in fireBaseHistoryData){
            if ( historyDataList.find {it.id == i.id}==null){
                fireBaseListNotInHistoryList.add(i)
            }
            else{
                historyDataList.minus(i)
            }
        }

            if(historyDataList.find { it.isSynced } !=null){
                if(fireBaseListNotInHistoryList.isNotEmpty()){
                    FireStoreClass().deleteHistoryDataFromFireStore(fireBaseListNotInHistoryList)
                }
            }
            else{
                if(fireBaseListNotInHistoryList.isNotEmpty()){
                    for (historyData in fireBaseListNotInHistoryList){
                        databaseDao.insertInHistory(historyData)

                    }


                }
            }
            if(historyDataList.isNotEmpty()){
                FireStoreClass().addCustomerHistoryDataToFireStore(historyDataList)

                for(i in historyDataList){
                    databaseDao.updateHistory(i.copy(isSynced = true))
                }
        }
    }

    private suspend  fun somethingWrongSnackBar(){
        SnackBarController.sendEvent(
            event = SnackBarEvent(
                message = "Something Went Wrong",
                action = SnackBarAction(name = "X")
            )
        )
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network= connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
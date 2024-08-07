package com.example.dairyman

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DairyViewModel:ViewModel(){

    private var  name = mutableStateOf("")
    private var rate = mutableStateOf("")
    private var amount = mutableStateOf("")
    private var pendingAmount = mutableStateOf("")

    lateinit var getAllDairyData: Flow<List<DairyData>>
//    private var updateDataList: List<DairyData> = emptyList()

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


    fun addDairyData(userData:DairyData){
        viewModelScope.launch(IO){
            dairyDao.insertDairyData(userData)
        }

    }

    fun updateTodayAmount(){
        viewModelScope.launch(IO){
            dairyDao.updateTodayAmount()
        }

    }
}
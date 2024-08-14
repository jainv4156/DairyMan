package com.example.dairyman.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
@Dao
interface DatabaseDao {

    @Upsert
    suspend fun upsertDairyData(dairyData:DairyData)

    @Insert
    suspend fun insertHistory(historyData: List<HistoryData>)

    @Query("SELECT * FROM DairyTable WHERE amount!=tempAmount")
    suspend fun getDataToRecord():List<DairyData>

    @Query("SELECT * FROM DairyTable WHERE id=:id")
    fun getDairyDataById(id:Long):Flow<DairyData>

    @Delete
    fun deleteDairyData(data:DairyData)

    @Query("SELECT * FROM DairyTable")
    fun loadAllDairyData():Flow<List<DairyData>>

    @Update
    suspend fun updateDairyData(dairyData: DairyData)

    @Query("UPDATE DairyTable SET pendingAmount=pendingAmount+CAST((rate*tempAmount) AS INTEGER)")
    suspend fun updateTodayAmount()
}


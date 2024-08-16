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
    suspend fun upsertDairyData(dairyData: DairyData)

    @Insert
    suspend fun insertHistory(historyData: List<HistoryData>)

    @Query("SELECT * FROM DairyTable WHERE amount!=tempAmount")
    suspend fun getDataToRecord():List<DairyData>

    @Query("SELECT * FROM DairyTable WHERE id=:id")
    fun getDairyDataById(id:Long):Flow<DairyData>

    @Delete
    fun deleteDairyData(data: DairyData)

    @Query("DELETE FROM historyDataTable WHERE dataId=:dataId")
    suspend fun deleteHistoryData(dataId: Long)

    @Query("SELECT * FROM DairyTable")
    fun loadAllDairyData():Flow<List<DairyData>>

    @Update
    suspend fun updateDairyData(dairyData: DairyData)

    @Query("UPDATE DairyTable SET pendingAmount=pendingAmount+CAST((rate*tempAmount) AS INTEGER)")
    suspend fun updateTodayAmount()

    @Query("SELECT DairyTable.name,DairyTable.rate,historyDataTable.amount,historyDataTable.tempAmount,historyDataTable.date FROM DairyTable JOIN historyDataTable ON DairyTable.id= historyDataTable.dataId WHERE historyDataTable.dataId=:id")
    fun getHistoryById(id:Long):Flow<List<JoinedResult>>
    @Query("SELECT COUNT(*) FROM historyDataTable WHERE Date=:date")
    suspend fun getHistoryCount(date:String):Int
}


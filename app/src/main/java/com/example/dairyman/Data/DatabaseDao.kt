package com.example.dairyman.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.Data.Model.HistoryData
import com.example.dairyman.Data.Model.JoinedResult
import kotlinx.coroutines.flow.Flow
@Dao
interface DatabaseDao {

    @Upsert
    suspend fun upsertDairyData(dairyData: DairyData)

    @Query("SELECT * FROM DairyTable WHERE id=:id")
    fun getDairyDataById(id:Long):Flow<DairyData>

    @Delete
    fun deleteDairyData(data: DairyData)

    @Query("SELECT * FROM DairyTable")
    fun loadAllDairyData():Flow<List<DairyData>>

    @Query("UPDATE DairyTable SET pendingAmount=pendingAmount+CAST((rate*tempAmount) AS INTEGER)")
    suspend fun updateTodayAmount()


    @Query("DELETE FROM historyDataTable WHERE dataId=:dataId")
    suspend fun deleteHistoryData(dataId: Long)

    @Query("SELECT * FROM historyDataTable")
    suspend fun getAllHistory():List<HistoryData>

    @Query("SELECT COUNT(*) FROM historyDataTable WHERE Date=:date")
    suspend fun getHistoryCount(date:String):Int

    @Insert
    suspend fun insertHistory(historyData: List<HistoryData>)
    @Update
    suspend fun updateHistory(historyData: HistoryData)



    @Query("SELECT DairyTable.name,DairyTable.rate,historyDataTable.amount,historyDataTable.tempAmount,historyDataTable.date FROM DairyTable JOIN historyDataTable ON DairyTable.id= historyDataTable.dataId WHERE historyDataTable.dataId=:id ORDER BY historyDataTable.id DESC" )
    fun getHistoryById(id:Long):Flow<List<JoinedResult>>

}


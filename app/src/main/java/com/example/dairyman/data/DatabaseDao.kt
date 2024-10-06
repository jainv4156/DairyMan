package com.example.dairyman.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.data.model.HistoryData
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

    @Query("SELECT COUNT(*) FROM historyDataTable WHERE Date >=:date")
    suspend fun getHistoryCount(date:Long):Int

    @Insert
    suspend fun insertHistory(historyData: List<HistoryData>)
    @Update
    suspend fun updateHistory(historyData: HistoryData)



    @Query("SELECT id,amount,rate,date,dataId,isSynced FROM historyDataTable  WHERE historyDataTable.dataId=:id ORDER BY historyDataTable.id DESC" )
    fun getHistoryById(id:Long):Flow<List<HistoryData>>

}


package com.example.dairyman

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Insert
    fun insertDairyData(dairyData:DairyData)

    @Query("SELECT * FROM DairyTable")
    fun loadAllDairyData():Flow<List<DairyData>>

    @Update
    fun updateDairyData(dairyData: DairyData)

}
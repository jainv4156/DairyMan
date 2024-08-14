package com.example.dairyman.Data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "DairyTable")
data class DairyData(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,
    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name = "rate")
    val rate:Int,
    @ColumnInfo(name = "amount")
    val amount:Float,
    @ColumnInfo(name = "pendingAmount")
    val pendingAmount:Int,
    @ColumnInfo(name = "tempAmount")
    val tempAmount:Float,
    @ColumnInfo(name = "DateCreated")
    val dateCreated:Long=System.currentTimeMillis(),
    @ColumnInfo(name = "DateUpdated")
    val dateUpdated:Long=System.currentTimeMillis(),
    @ColumnInfo(name = "dayForTempAmount")
    val dayForTempAmount:Int=0
)
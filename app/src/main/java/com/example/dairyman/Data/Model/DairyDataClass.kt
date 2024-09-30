package com.example.dairyman.Data.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DairyTable")
data class DairyData(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,
    @ColumnInfo(name = "name")
    val name:String="",
    @ColumnInfo(name = "rate")
    val rate:Int=0,
    @ColumnInfo(name = "amount")
    val amount:Float=0F,
    @ColumnInfo(name = "pendingAmount")
    val pendingAmount:Int=0,
    @ColumnInfo(name = "tempAmount")
    val tempAmount:Float=0F,
    @ColumnInfo(name = "DateCreated")
    val dateCreated:Long=System.currentTimeMillis(),
    @ColumnInfo(name = "DateUpdated")
    val dateUpdated:Long=System.currentTimeMillis(),
    @ColumnInfo(name = "dayForTempAmount")
    val dayForTempAmount:Int=0
)
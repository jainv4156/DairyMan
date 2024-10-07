package com.example.dairyman.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "DairyTable")
data class DairyData(
    @PrimaryKey
    val id:String= UUID.randomUUID().toString(),
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
    val dayForTempAmount:Int=0,
    @ColumnInfo(name = "isSynced")
    val isSynced:Boolean=false,
    @ColumnInfo(name="isSuspended")
    val isSuspended:Boolean=false
)
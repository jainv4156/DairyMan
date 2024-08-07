package com.example.dairyman

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DairyTable")
data class DairyData(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,

    @ColumnInfo(name = "name")
    val name:String,
    @ColumnInfo(name = "rate")
    val rate:Int,
    @ColumnInfo(name="amount")
    val amount:Float,
    @ColumnInfo(name="pendingAmount")
    val pendingAmount:Int,
    @ColumnInfo(name="tempAmount")
    val tempAmount:Float,
)

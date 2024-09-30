package com.example.dairyman.Data.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "historyDataTable", foreignKeys = [ForeignKey(entity = DairyData::class, childColumns = ["dataId"], parentColumns = ["id"])])
data class HistoryData(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,
    @ColumnInfo(name="amount")
    val amount:Float=0F,
    @ColumnInfo(name="tempAmount")
    val tempAmount:Float=0f,
    @ColumnInfo(name ="Date")
    val date: String,
    @ColumnInfo(name = "dataId")
    val dataId:Long=0L,
)

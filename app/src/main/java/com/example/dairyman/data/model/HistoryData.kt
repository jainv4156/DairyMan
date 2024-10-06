package com.example.dairyman.data.model

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
    @ColumnInfo(name="rate")
    val rate:Int=0,
    @ColumnInfo(name ="date")
    val date: Long=System.currentTimeMillis(),
    @ColumnInfo(name = "dataId")
    val dataId:Long=0L,
    @ColumnInfo(name = "isSynced")
    val isSynced:Boolean=false
)

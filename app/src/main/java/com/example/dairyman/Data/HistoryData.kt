package com.example.dairyman.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "historyDataTable", foreignKeys = [ForeignKey(entity = DairyData::class, childColumns = ["dataId"], parentColumns = ["id"])])
data class HistoryData(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,
    @ColumnInfo(name="amount")
    val amount:Float,
    @ColumnInfo(name="tempAmount")
    val tempAmount:Float,
    @ColumnInfo(name ="Date")
    val date:Long=System.currentTimeMillis(),
    @ColumnInfo(name = "dataId")
    val dataId:Long,
)

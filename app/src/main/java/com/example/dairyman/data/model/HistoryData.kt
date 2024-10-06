package com.example.dairyman.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "historyDataTable", foreignKeys = [ForeignKey(entity = DairyData::class, childColumns = ["dataId"], parentColumns = ["id"], onDelete = ForeignKey.CASCADE)])
data class HistoryData(
    @PrimaryKey
    val id:String= UUID.randomUUID().toString(),
    @ColumnInfo(name="amount")
    val amount:Float=0F,
    @ColumnInfo(name="rate")
    val rate:Int=0,
    @ColumnInfo(name ="date")
    val date: Long=System.currentTimeMillis(),
    @ColumnInfo(name = "dataId")
    val dataId:String="",
    @ColumnInfo(name = "isSynced")
    val isSynced:Boolean=false
)

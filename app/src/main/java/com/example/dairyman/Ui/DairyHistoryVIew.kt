package com.example.dairyman.Ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.dairyman.DairyViewModel
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DairyHistoryVIew(id: Long) {

    Scaffold(modifier = Modifier.fillMaxSize()) {
        val history= DairyViewModel().getHistoryById(id).collectAsState(initial = listOf())
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            LazyColumn (modifier = Modifier.fillMaxWidth()){
                items(history.value){item->
                    Row {
                        val simpleDate=SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val date=simpleDate.format(item.Date)

                        Text(text = item.name)
                        Text(text = item.rate.toString())
                        Text(text = item.tempAmount.toString()+"("+item.amount.toString()+")")
                        Text(text = date)
                    }

                }
            }
        }
    }

}
@Serializable
data class ScreenC(
    val id:Long
)

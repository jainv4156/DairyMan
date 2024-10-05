package com.example.dairyman.uiComponent.CustomerProfilePage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dairyman.Data.Model.HistoryData
import com.example.dairyman.ui.theme.GoodGreen
import com.example.dairyman.viewmodel.DairyViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerHistoryView(id:Long){
    val historyList=DairyViewModel().getHistoryById(id).collectAsState(initial = listOf())
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = "History", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        LazyColumn (){
            items(historyList.value){item ->
                HistoryListView(item)
            }
        }
    }
}
@Composable
fun HistoryListView(item:HistoryData){
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()){
        Row {
            Text(text = "${item.amount}Kg",fontSize = 20.sp)
            Text(text = "(Rs${item.rate})", color = GoodGreen,fontSize = 20.sp)
        }
        val date= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(item.date)
        Text(text = date, fontSize = 20.sp)
    }
}
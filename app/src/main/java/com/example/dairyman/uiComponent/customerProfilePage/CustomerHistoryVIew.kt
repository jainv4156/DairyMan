package com.example.dairyman.uiComponent.customerProfilePage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dairyman.data.model.HistoryData
import com.example.dairyman.ui.theme.GoodGreen
import com.example.dairyman.viewmodel.DairyViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerHistoryView(id:String){
    val historyList=DairyViewModel().getHistoryById(id).collectAsState(initial = listOf())
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(12.dp,20.dp)) {
        Text(text = "History", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier =Modifier.padding(bottom = 16.dp,top=16.dp))

        LazyColumn {
            items(historyList.value){item ->
                HistoryListView(item)
            }
        }
    }
}
@Composable
fun HistoryListView(item:HistoryData){
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(12.dp)){
        Row {
            Text(text = "${item.amount}Kg",fontSize = 18.sp)
            Text(text = "(Rs${item.rate})", color = GoodGreen,fontSize = 18.sp)
        }
        val date= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(item.date)
        Text(text = date, fontSize = 18.sp)
    }
}
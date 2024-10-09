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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dairyman.data.model.HistoryData
import com.example.dairyman.ui.theme.CancelRed
import com.example.dairyman.ui.theme.GoodGreen
import com.example.dairyman.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerHistoryView(id:String){
    val historyList=HomeViewModel().getHistoryById(id).collectAsState(initial = listOf())
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp, 20.dp)) {
        Text(text = "History", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier =Modifier.padding(bottom = 16.dp,top=16.dp))
        if(historyList.value.isEmpty()){
            Text(text = "No History Yet ", color = Color.Red, fontWeight = FontWeight.Medium, fontSize = 20.sp, modifier =Modifier.padding(bottom = 16.dp,top=16.dp))
        }
        else{
            LazyColumn {
                items(historyList.value.sortedByDescending { it.date  }){item ->
                    HistoryListView(item)
                }
            }
        }
    }
}
@Composable
fun HistoryListView(item:HistoryData){
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp,14.dp)){
        if(item.balanceChange!=0){
            if(item.balanceChange<0){
                Text(text = "Rs${-item.balanceChange} Received" , color = GoodGreen,fontSize = 18.sp)
            }
            else{
                Text(text = "Rs${item.balanceChange} Added " , color = CancelRed,fontSize = 18.sp)
            }
        }
        else{

            Row {
                Text(text = "${item.amount}Kg",fontSize = 18.sp)
                Text(text = "(Rs${item.rate})",fontSize = 18.sp)
            }
        }
        val date= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(item.date)
        Text(text = date, fontSize = 18.sp)
    }
}
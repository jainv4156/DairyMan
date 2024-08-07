package com.example.dairyman

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Composable
fun StartingView(navController: NavController,viewModel: DairyViewModel) {

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = { FloatingActionButton {
        navController.navigate(
            ScreenB
        )
    }
    } ) {
//        Button(onClick = { viewModel.updateTodayAmount() }) {
//            Text(text = "UpdateTodayMoney")
//        }
//        Spacer(modifier = Modifier.height(10.dp))

        val dairyList = viewModel.getAllDairyData.collectAsState(initial = listOf())
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            items(dairyList.value) { item->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = item.name)
                    Text(text = item.rate.toString())
                    Text(text = item.amount.toString())
                    Text(text = item.pendingAmount.toString())
                }
            }
        }
    }
}
@Serializable
object ScreenA
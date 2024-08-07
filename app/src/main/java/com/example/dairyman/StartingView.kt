package com.example.dairyman

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Composable
fun StartingView(navController: NavController,viewModel: DairyViewModel) {

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = { FloatingActionButton {
        navController.navigate(
            ScreenB(id = 0L)
        )
    }
    } ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Button(modifier = Modifier.padding(16.dp),onClick = { viewModel.updateTodayAmount() }) {
                Text(text = "UpdateTodayMoney")
            }
            val dairyList = viewModel.getAllDairyData.collectAsState(initial = listOf())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(dairyList.value) { item ->
                    ShowDataView(item,navController,viewModel)
                }
            }

        }
    }



}

@Composable
fun ShowDataView(item:DairyData,navController: NavController,viewModel: DairyViewModel){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = item.name)
        Text(text = item.rate.toString())
        Text(text = item.amount.toString())
        Text(text = item.pendingAmount.toString())
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(modifier = Modifier.padding(16.dp),onClick = { navController.navigate(ScreenB(item.id)) }) {
            Text(text = "Update")
        }
        Button(modifier = Modifier.padding(16.dp),onClick = { viewModel.deleteDataById(item) }) {
            Text(text = "Delete")
        }
    }
}



@Serializable
object ScreenA

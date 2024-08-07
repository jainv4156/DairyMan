package com.example.dairyman

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Composable
fun AddNewRecordView(
    navController: NavController,
    viewModel: DairyViewModel
){

    Scaffold(modifier = Modifier.fillMaxSize()) {it->

        Column (modifier = Modifier.fillMaxSize()){
            OutlinedTextField(modifier=Modifier.fillMaxWidth(),value = viewModel.getName() , onValueChange = {viewModel.setName(it)})
            OutlinedTextField(value =viewModel.getRate() , onValueChange = { viewModel.setRate(it) })
            OutlinedTextField(value =viewModel.getAmount() , onValueChange = { viewModel.setAmount(it) })
            OutlinedTextField(value =viewModel.getPendingAmount() , onValueChange = { viewModel.setPendingAmount(it) })
            Button(onClick = {
                viewModel.addDairyData(DairyData(name = viewModel.getName(), rate = viewModel.getRate().toInt(), amount = viewModel.getAmount().toFloat(), pendingAmount = viewModel.getPendingAmount().toInt(), tempAmount = viewModel.getPendingAmount().toFloat()))
                navController.navigate(ScreenA) }, modifier = Modifier.padding(it)) {
               Text(text = "Add data")
            }

        }
    }
}
@Serializable
object ScreenB
package com.example.dairyman

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dairyman.Data.DairyData
import kotlinx.serialization.Serializable

@Composable
fun AddNewRecordView(
    navController: NavController,
    viewModel: DairyViewModel,
    id:Long
){
    if(id!=0L){
        val dairyData = viewModel.getDairyDataById(id).collectAsState(initial = DairyData(0,"",0,0F,0,0F))
        viewModel.setName(dairyData.value.name)
        viewModel.setRate(dairyData.value.rate.toString())
        viewModel.setAmount(dairyData.value.amount.toString())
        viewModel.setPendingAmount(dairyData.value.pendingAmount.toString())
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {it->


        Column (modifier = Modifier.fillMaxSize()){
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(modifier=Modifier.fillMaxWidth(),value = viewModel.getName() , onValueChange = {viewModel.setName(it)})
            OutlinedTextField(value =viewModel.getRate() , onValueChange = { viewModel.setRate(it) })
            OutlinedTextField(value =viewModel.getAmount() , onValueChange = { viewModel.setAmount(it) })
            OutlinedTextField(value =viewModel.getPendingAmount() , onValueChange = { viewModel.setPendingAmount(it) })
            if(id==0L){

                Button(onClick = {
                    viewModel.addUpdateDairyData(DairyData(name = viewModel.getName(), rate = viewModel.getRate().toInt(), amount = viewModel.getAmount().toFloat(), pendingAmount = viewModel.getPendingAmount().toInt(), tempAmount = viewModel.getAmount().toFloat()))
                    navController.navigate(ScreenA) }, modifier = Modifier.padding(it)) {
                    Text(text = "Add data")
                }

            }
            else{

                Button(onClick = {
                    viewModel.addUpdateDairyData(DairyData(id=id,name = viewModel.getName(), rate = viewModel.getRate().toInt(), amount = viewModel.getAmount().toFloat(), pendingAmount = viewModel.getPendingAmount().toInt(), tempAmount = viewModel.getAmount().toFloat(), dateUpdated = System.currentTimeMillis()))
                    navController.navigate(ScreenA) }, modifier = Modifier.padding(it)) {
                   Text(text = "Update data")
                }
            }


        }
    }
}
@Serializable
data class ScreenB(
    val id:Long
)
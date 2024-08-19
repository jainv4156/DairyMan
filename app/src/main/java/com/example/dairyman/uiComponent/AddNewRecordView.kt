package com.example.dairyman.uiComponent

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.DairyViewModel
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
    val context= LocalContext.current

    Scaffold(topBar = { TopAppBarView(title = if(id==0L) "Add New Record" else "Update Record",onBackNavClicked = {navController.navigate(ScreenA)}) },modifier = Modifier
        .fillMaxSize()
    ) {padding->
        Box(modifier = Modifier.fillMaxSize().padding(24.dp),contentAlignment=Alignment.Center) {
        Column (modifier = Modifier.padding(bottom = 150.dp),horizontalAlignment = Alignment.CenterHorizontally){
                OutlinedTextFieldStyle(
                    viewModel.getName(),
                    onValueChange = { viewModel.setName(it) },
                    title = "Name"
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextFieldStyle(
                    value = viewModel.getRate(),
                    onValueChange = { viewModel.setRate(it) },
                    title = "Rate"
                )
                Spacer(modifier = Modifier.height(24.dp).padding(padding))
                OutlinedTextFieldStyle(
                    value = viewModel.getAmount(),
                    onValueChange = { viewModel.setAmount(it) },
                    title = "Quantity(kg)"
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextFieldStyle(
                    value = viewModel.getPendingAmount(),
                    onValueChange = { viewModel.setPendingAmount(it) },
                    title = "Previous Balance"
                )
                if (id == 0L) {
                    Button(onClick = {
                        if(viewModel.getName()==""||viewModel.getRate()==""||viewModel.getAmount()==""){
                            Toast.makeText(context,"Please Fill All The Fields",Toast.LENGTH_SHORT).show()
                        }else{


                        viewModel.addUpdateDairyData(
                            DairyData(
                                name = viewModel.getName(),
                                rate = viewModel.getRate().toInt(),
                                amount = viewModel.getAmount().toFloat(),
                                pendingAmount = viewModel.getPendingAmount().toInt(),
                                tempAmount = viewModel.getAmount().toFloat()
                            )
                        )
                        navController.navigate(ScreenA)
                    }}, modifier = Modifier.padding(top = 24.dp).fillMaxWidth()) {
                        Text(text = "Add Record", modifier = Modifier.padding(8.dp), fontSize = 18.sp)
                    }

                } else {

                    Button(onClick = {
                        viewModel.addUpdateDairyData(
                            DairyData(
                                id = id,
                                name = viewModel.getName(),
                                rate = viewModel.getRate().toInt(),
                                amount = viewModel.getAmount().toFloat(),
                                pendingAmount = viewModel.getPendingAmount().toInt(),
                                tempAmount = viewModel.getAmount().toFloat(),
                                dateUpdated = System.currentTimeMillis()
                            )
                        )
                        navController.navigate(ScreenA)
                    }, modifier = Modifier.padding(top = 24.dp).fillMaxWidth()) {
                        Text(text = "Update Record")
                    }
                }
            }
        }
    }
}


@Serializable
data class  ScreenB(
    val id:Long
)
package com.example.dairyman.uiComponent

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.uiComponent.homeScreen.ScreenA
import com.example.dairyman.viewmodel.AddUpdateCustomerDetailViewModel
import kotlinx.serialization.Serializable

@Composable
fun AddUpdateCustomerDetailView(
    navController: NavController,
    viewModel: AddUpdateCustomerDetailViewModel,
    id:Long=0L
){
    val context= LocalContext.current
    if(id!=0L){
        viewModel.preFillUpdateInput(id=id)
    }

    Scaffold(topBar = { TopAppBarView(title = if(id==0L) "Add New Record" else "Update Record",onBackNavClicked = {navController.navigate(
        ScreenA
    )}) },modifier = Modifier
        .fillMaxSize()
    ) {padding->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),contentAlignment=Alignment.Center) {
        Column (modifier = Modifier.padding(bottom = 150.dp),horizontalAlignment = Alignment.CenterHorizontally){
                OutlinedTextFieldStyle(
                    viewModel.fetchAddUpdateCustomerDetailData().name,
                    onValueChange = { viewModel.setAddUpdateCustomerDetailData(newAddUpdateCustomerDetailModel = viewModel.fetchAddUpdateCustomerDetailData().copy(name = it)) },
                    title = "Name",
                    keyboardOptions = KeyboardType.Text
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextFieldStyle(
                    value = viewModel.fetchAddUpdateCustomerDetailData().rate,
                    onValueChange = { viewModel.setAddUpdateCustomerDetailData(newAddUpdateCustomerDetailModel = viewModel.fetchAddUpdateCustomerDetailData().copy(rate = it)) },
                    title = "Rate",
                    keyboardOptions = KeyboardType.Number
                )
                Spacer(modifier = Modifier
                    .height(24.dp)
                    .padding(padding))
                OutlinedTextFieldStyle(
                    value = viewModel.fetchAddUpdateCustomerDetailData().amount,
                    onValueChange = { viewModel.setAddUpdateCustomerDetailData(newAddUpdateCustomerDetailModel = viewModel.fetchAddUpdateCustomerDetailData().copy(amount = it)) },
                    title = "Quantity(kg)"
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextFieldStyle(
                    value =viewModel.fetchAddUpdateCustomerDetailData().pendingAmount,
                    onValueChange = { viewModel.setAddUpdateCustomerDetailData(newAddUpdateCustomerDetailModel = viewModel.fetchAddUpdateCustomerDetailData().copy(pendingAmount = it)) },
                    title = "Previous Balance"
                )
            Box(modifier = Modifier
                .padding( 0.dp,24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Primary)
                .padding( 16.dp)
                .clickable { addOrUpdateButtonOnClick(
                    navController,
                    id =id,
                    viewModel = viewModel,
                    context = context
                ) }
                .fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Text(text = if (id == 0L) "Add Record" else "Update Record", color = Background, fontSize = 16.sp)
            }
        }
        }
    }
}
private fun addOrUpdateButtonOnClick(navController: NavController, viewModel: AddUpdateCustomerDetailViewModel, id: Long, context: Context) {
    if(viewModel.fetchAddUpdateCustomerDetailData().name==""||viewModel.fetchAddUpdateCustomerDetailData().rate==""||viewModel.fetchAddUpdateCustomerDetailData().amount==""){
        Toast.makeText(context,"Please Fill All The Fields",Toast.LENGTH_SHORT).show()
    }else{
        viewModel.addUpdateDairyData(id=id)
        navController.navigate(ScreenA)

    }
}

@Serializable
data class  ScreenB(
    val id:Long=0L
)
package com.example.dairyman.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.DairyViewModel
import com.example.dairyman.Data.DairyData
import kotlinx.serialization.Serializable

@Composable
fun StartingView(navController: NavController,viewModel: DairyViewModel) {
    val isLongPressed = remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton {
            navController.navigate(
                ScreenB(id = 0L)
            )
        }
    }) {

        Column(modifier = Modifier.padding(30.dp)) {
            Spacer(modifier = Modifier.height(it.calculateTopPadding()))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                onClick = {viewModel.checkTodayUpdate()}) {
                Text(text = "UpdateTodayMoney", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(8.dp))
            }
            val dairyList = viewModel.getAllDairyData.collectAsState(initial = listOf())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(dairyList.value) { item ->
                    ShowDataView(item, navController, viewModel,isLongPressed)
                }
            }

        }

    }
    if(isLongPressed.value){
        BlurredBackground()
        ChangeAmountScreen(isLongPressed,viewModel)
    }
    if(viewModel.getIsAlertDialogBox().value){
        BlurredBackground()
        AlertDialogBoxView(viewModel)
    }



}

    @Composable
    fun ShowDataView(item: DairyData, navController: NavController, viewModel: DairyViewModel, isLongPressed: MutableState<Boolean>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clickable { navController.navigate(ScreenC(id = item.id)) },
        ) {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = item.name.substring(0, 1).uppercase() + item.name.substring(1).lowercase(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = item.rate.toString()+"Rs", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Row (modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Column{
                    val subContentColor=if(isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
                Text(color = subContentColor,text = "Quantity: "+if(item.amount==item.tempAmount) item.tempAmount.toString()+"kg" else item.tempAmount.toString()+"Kg"+" for "+item.dayForTempAmount+if(item.dayForTempAmount==1)" Day" else " Days",
                    modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures (
                                onLongPress= {
                                    val tempAmounts = item.tempAmount.toString() // Update here
                                    viewModel.setAmount(tempAmounts)
                                    viewModel.setDairyDataFromAmountPressed(item)
                                    isLongPressed.value = true
                                }
                            )
                        }
                )
                Text(color = subContentColor, text = "Pending Amount:"+item.pendingAmount.toString())

            }
                Row{
                IconButton(
                    onClick = { navController.navigate(ScreenB(item.id)) }) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit Button", tint = Color.Gray)
                }
                IconButton(
                    onClick = { viewModel.deleteDataById(item) }) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete Button", tint = Color.Gray    )
                }
            }
            }

        }

    }

@Composable
fun BlurredBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .blur(radius = 50.dp)
    )
}


@Serializable
object ScreenA

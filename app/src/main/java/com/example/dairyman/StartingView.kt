package com.example.dairyman

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dairyman.Data.DairyData
import kotlinx.serialization.Serializable

@Composable
fun StartingView(navController: NavController,viewModel: DairyViewModel) {
    var isLongPressed = remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton {
            navController.navigate(
                ScreenB(id = 0L)
            )
        }
    }) {

        Column {

            Spacer(modifier = Modifier.height(it.calculateTopPadding()))
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = { viewModel.updateTodayAmountButton() }) {
                Text(text = "UpdateTodayMoney")
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



}

    @Composable
    fun ShowDataView(item: DairyData, navController: NavController, viewModel: DairyViewModel, isLongPressed: MutableState<Boolean>) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(text = item.name)

            Text(text = item.rate.toString())

            Text(text = if(item.amount==item.tempAmount) item.tempAmount.toString() else item.tempAmount.toString()+"("+item.amount.toString()+")",

                modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures (
                        onLongPress= {
                            val tempAmounts = item.tempAmount.toString() // Update here
                            viewModel.setAmount(tempAmounts)
                            viewModel.setDairyDataFromAmountPressed(item)
                            isLongPressed.value = true
                        }
                    )

            })
            Text(text = item.pendingAmount.toString())
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = { navController.navigate(ScreenB(item.id)) }) {
                Text(text = "Update")
            }
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = { viewModel.deleteDataById(item) }) {
                Text(text = "Delete")
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

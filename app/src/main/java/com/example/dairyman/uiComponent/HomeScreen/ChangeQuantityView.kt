package com.example.dairyman.uiComponent.HomeScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dairyman.uiComponent.OutlinedTextFieldStyle
import com.example.dairyman.viewmodel.DairyViewModel

@Composable
fun ChangeAmountScreen(viewModel: DairyViewModel){
    Box (modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
        contentAlignment = Alignment.Center){

        Column (modifier = Modifier
            .align(Alignment.Center)
            .clip(RoundedCornerShape(8.dp))
            .background(if(isSystemInDarkTheme()) Color.DarkGray else Color.LightGray)
            .padding(32.dp,16.dp),
            horizontalAlignment = Alignment.CenterHorizontally){

            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextFieldStyle(value = viewModel.getTempAmount(), onValueChange = {viewModel.setTempAmount(it)}, title = "Quantity", keyboardOptions = KeyboardType.Number)
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextFieldStyle(value = viewModel.getDayForTempAmount(), onValueChange = {viewModel.setDayForTempAmount(it)}, title = "No Of Days", keyboardOptions = KeyboardType.Number)
            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = { viewModel.disableSetTempAmountView() }) {
                    Text(text = "Cancel")
                }
                Button(onClick = {
                    viewModel.updateTempAmountView()
                }) {
                    Text(text = "Add")

                }
            }
        }
    }
}
package com.example.dairyman.uiComponent

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.R
import com.example.dairyman.ui.theme.WarningDark
import com.example.dairyman.ui.theme.WarningLight

@Composable
fun AlertDialogBoxView( viewModel: DairyViewModel) {
        Box (modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .pointerInput(Unit) {},
            contentAlignment = Alignment.Center){
            val colorOnThemeBase=  if(isSystemInDarkTheme()) colorResource(R.color.white) else colorResource(
                R.color.black)
            val shape = RoundedCornerShape(8.dp)
            Column (modifier = Modifier
                .align(Alignment.Center)
                .clip(shape)
                .background(if(isSystemInDarkTheme()) Color.DarkGray else Color.LightGray)
                .padding(32.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(color =colorOnThemeBase ,text = "Warning!", fontSize = 18.sp, fontWeight = FontWeight.W800)
                Spacer(modifier = Modifier.height(32.dp))
                Text(fontSize = 18.sp, color = if(isSystemInDarkTheme()) WarningDark else WarningLight,text = "You Have Added Today's Amount Do You Wish To Continue adding the Amount")
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    Button(onClick = { viewModel.setAlertDialogBox(false)}) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        viewModel.updateTodayAmountButton()
                        viewModel.setAlertDialogBox(false) }) {
                        Text(text = "Continue")
                    }
                }
            }
        }
}
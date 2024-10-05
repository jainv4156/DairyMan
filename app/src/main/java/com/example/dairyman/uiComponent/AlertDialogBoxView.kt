package com.example.dairyman.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.DarkBackground

@Composable
fun AlertDialogBoxView( viewModel: DairyViewModel,navController: NavController) {
        Box (modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
            contentAlignment = Alignment.Center){
            val shape = RoundedCornerShape(8.dp)
            Column (modifier = Modifier
                .align(Alignment.Center)
                .clip(shape)
                .background(Background)
                .padding(32.dp, 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Spacer(modifier = Modifier.height(32.dp))
                Text(fontSize = 18.sp, color =  Color.Red,text = viewModel.getAlertDialogTitle())
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                    Box (modifier = Modifier
                        .shadow(
                            elevation = 3.dp,
                            RoundedCornerShape(12.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                        .padding(1.dp, 0.dp, 1.dp, 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = DarkBackground)
                        .padding(16.dp, 4.dp)
                        .clickable {
                            viewModel.resetHomeViewState()
                        }
                    ){
                        Text(fontWeight = FontWeight.Medium,text = "Cancel")
                    }
                    Box (modifier = Modifier
                        .shadow(
                            elevation = 3.dp,
                            RoundedCornerShape(12.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                        .padding(1.dp, 0.dp, 1.dp, 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = DarkBackground)
                        .padding(16.dp, 4.dp)
                        .clickable {
                            onAlertClick(viewModel, navController =navController)
                        }
                    ){
                        Text(fontWeight = FontWeight.Medium,text = "Continue")
                    }
                }



            }
        }
}

fun onAlertClick(viewModel: DairyViewModel,navController: NavController) {
    if(viewModel.getSignInAlertBox()){
        navController.navigate(ScreenD)
    }
     if(viewModel.getIsDeleteAlertEnabled() != DairyData(id=0)){
        viewModel.deleteDataById()
        viewModel.resetHomeViewState()
    }
    if(viewModel.getIsUpdateAmountAlertEnable()){
        viewModel.updateTodayAmountButton()
        viewModel.resetHomeViewState()
    }
}

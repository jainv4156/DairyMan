package com.example.dairyman.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.ui.theme.Secondary
import com.example.dairyman.viewmodel.DairyViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun FloatingActionButtonView(
    isActionButtonExtended: MutableState<Boolean>,
    viewModel: DairyViewModel,
    toggleActinButton: () -> Unit,
    navController: NavController
){
    Column ( horizontalAlignment = Alignment.End){
    if(isActionButtonExtended.value){
        ActionButton(viewModel,navController,toggleActinButton=toggleActinButton)
    }
    FloatingActionButton(
        containerColor= if (isActionButtonExtended.value) Primary else  Secondary,
        elevation = FloatingActionButtonDefaults.elevation(10.dp ),
        contentColor = contentColorFor(containerColor),
        onClick =  toggleActinButton,

    ) {
        Icon(imageVector = Icons.Default.Add,contentDescription = null, tint = if (isActionButtonExtended.value) Color.White else  Color.Black)
    }
    }
}
@Composable

fun ActionButton(viewModel: DairyViewModel,navController: NavController,toggleActinButton:()->Unit){

    val scope=rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {

        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Secondary)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .clickable {
                    scope.launch {
                        viewModel.syncDataWithCloud()
                    }
                }
        ) {
            Text(text = "Sync", modifier = Modifier.padding(10.dp), fontSize = 18.sp)
        }
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Secondary)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .clickable { navController.navigate(ScreenB()) }
        ) {
            Text(text = "Add Customer", modifier = Modifier.padding(10.dp), fontSize = 18.sp)
        }
        Box(modifier = Modifier
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Secondary)
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .clickable {
                viewModel.checkTodayUpdate()
                toggleActinButton()
            }) {
            Text(text = "Update Today Amount", modifier = Modifier.padding(10.dp), fontSize = 18.sp)
        }
    }
}

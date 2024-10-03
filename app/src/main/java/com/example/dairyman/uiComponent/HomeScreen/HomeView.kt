package com.example.dairyman.uiComponent.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.Data.Model.DairyData
import com.example.dairyman.ui.theme.Accent
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.DarkBackground
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.ui.theme.Secondary
import com.example.dairyman.uiComponent.AlertDialogBoxView
import com.example.dairyman.uiComponent.ScreenB
import com.example.dairyman.uiComponent.ScreenC
import com.example.dairyman.uiComponent.ScreenD
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Composable
fun HomeView(
    navController: NavController,
    viewModel: DairyViewModel,
){
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(color = Background),
        floatingActionButton = {
            if (viewModel.getIsFloatingButtonVisible()) FloatingActionButtonView(
                viewModel = viewModel,
                navController = navController
            )
        },
        topBar = { HomeScreenTopView(title = "Dairyman", viewModel = viewModel, navController = navController
        ) },
    ) {

        if (viewModel.getIsEEditDeleteButtonEnabled()!=-1L) {
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { viewModel.disableMoreOption() }
                .zIndex(0f)
            )
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clickable(enabled = viewModel.getIsEEditDeleteButtonEnabled() != -1L) {
                    viewModel.disableMoreOption()
                }
        ) {
            val customersList = viewModel.getCustomersList().collectAsState(initial = listOf()).value
            LaunchedEffect(key1 = viewModel.getSearchQuery()) {
                runBlocking {  viewModel.getSearchFilteredList()}
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .zIndex(1f)
            ) {
                items(customersList) { item ->
                    Spacer(modifier = Modifier.height(16.dp))
                    ShowDataView(item, navController, viewModel)
                }
            }
        }
        if (viewModel.isActionButtonExtended.value ) {
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { viewModel.isActionButtonExtended.value = false }
            )
        }

    }
    if (viewModel.getIsSetTempAmountViewActive()) {
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { viewModel.disableSetTempAmountView() }
        )
        ChangeAmountScreen(viewModel)
    }
    if (viewModel.getIsAlertDialogBox().value) {
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { viewModel.disableAlertDialogBox() }
        )
        AlertDialogBoxView(alertTitle = "You Have Added Today's Amount Do You Wish To Continue adding the Amount",viewModel) {
            viewModel.updateTodayAmountButton()
            viewModel.setAlertDialogBox(false)
        }
    }
    if(viewModel.getSignInAlertBox()){
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { viewModel.disableAlertDialogBox() }
        )
        AlertDialogBoxView(alertTitle =  "You Have To SignIn before Syncing",viewModel){
            navController.navigate(ScreenD)
            viewModel.setAlertDialogBox(false)
        }
    }
}
@Composable
fun ShowDataView(item: DairyData, navController: NavController, viewModel: DairyViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        ){
        Column(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(color = Primary)
            .padding(16.dp)
            .clickable(enabled = viewModel.getIsEEditDeleteButtonEnabled() == -1L) {
                navController.navigate(
                    ScreenC(id = item.id)
                )
            }
            .zIndex(0f)
        ) {
        Text(
            color = Color.White,
            text = item.name.substring(0, 1).uppercase() + item.name.substring(1).lowercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column {
                Text(
                    color = Color.White,
                    text = "Quantity: " + if (item.amount == item.tempAmount) item.tempAmount.toString() + "kg" else item.tempAmount.toString() + "Kg" + " for " + item.dayForTempAmount + if (item.dayForTempAmount == 1) " Day" else " Days"
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier
                    .clickable {
                        viewModel.setIdTempAmount(id = item.id)
                        viewModel.readySetTempAmountView()
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Secondary)

                    ) {
                    Text(
                        modifier = Modifier.padding(8.dp, 4.dp),
                        text = "Change"
                    )
                    }
                    Icon(modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { viewModel.enableMoreOption(item.id) },
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "MoreOptions",
                        tint = Accent
                    )
                }
            }
        }
        if(viewModel.getIsEEditDeleteButtonEnabled() != -1L){
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .height(88.dp)
                .clickable { viewModel.disableMoreOption() }
            )
        }
        if(viewModel.getIsEEditDeleteButtonEnabled()==item.id){
            MoreOptionView(
                navController = navController, item = item)
        }
        }
}
@Composable
fun BlurredBackground(modifier: Modifier) {
    Box(
        modifier = modifier
    )
}
@Serializable
object ScreenA
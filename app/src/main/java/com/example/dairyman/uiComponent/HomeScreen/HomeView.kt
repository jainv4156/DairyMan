package com.example.dairyman.uiComponent.HomeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.dairyman.SnackBar.ObserveAsEvent
import com.example.dairyman.SnackBar.SnackBarController
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.uiComponent.AlertDialogBoxView
import com.example.dairyman.uiComponent.ScreenD
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: DairyViewModel,
){
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    ObserveAsEvent(
        flow = SnackBarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

             snackbarHostState.showSnackbar(
                message = event.message,
                duration = SnackbarDuration.Short
            )

        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(color = Background),
        floatingActionButton = {
            if (viewModel.getIsFloatingButtonVisible()) FloatingActionButtonView(
                viewModel = viewModel,
                navController = navController
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = { HomeScreenTopView(title = "Dairyman", viewModel = viewModel, navController = navController
        ) },
    ) {

        if (viewModel.getIsEEditDeleteButtonEnabled()!=-1L) {
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { viewModel.resetHomeViewState() }
                .zIndex(0f)
            )
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clickable(enabled = viewModel.getIsEEditDeleteButtonEnabled() != -1L) {
                    viewModel.resetHomeViewState()
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
            .clickable { viewModel.resetHomeViewState() }
        )
        ChangeAmountScreen(viewModel)
    }
    if (viewModel.getIsAlertDialogBox().value) {
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { viewModel.resetHomeViewState() }
        )
        AlertDialogBoxView(viewModel,navController)
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
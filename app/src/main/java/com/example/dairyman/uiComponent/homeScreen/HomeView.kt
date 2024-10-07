package com.example.dairyman.uiComponent.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.dairyman.snackBar.ObserveAsEvent
import com.example.dairyman.snackBar.SnackBarController
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.uiComponent.AlertDialogBoxView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: DairyViewModel,
){
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    ObserveAsEvent(
        flow = SnackBarController.events,
        snackBarHostState
    ) { event ->
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()

            val result = snackBarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Long
            )

            if(result == SnackbarResult.ActionPerformed) {
                snackBarHostState.currentSnackbarData?.dismiss()
            }
        }
    }
    val alpha by animateFloatAsState(
        targetValue =if (viewModel.getIsEEditDeleteButtonEnabled()!=""||viewModel.isActionButtonExtended.value||viewModel.getIsSetTempAmountViewActive() ||viewModel.getIsAlertDialogBox().value) 0.5f else 0f, label = ""
    )
    Scaffold(

        modifier = Modifier

            .fillMaxSize()
        ,
        floatingActionButton = {
            if (viewModel.getIsFloatingButtonVisible()) FloatingActionButtonView(
                viewModel = viewModel,
                navController = navController
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        topBar = {
            HomeScreenTopView(
                title = "Dairyman", viewModel = viewModel, navController = navController
            )
        },
    ) {

        if (viewModel.getIsEEditDeleteButtonEnabled()!="") {
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha))
                .clickable { viewModel.resetHomeViewState() }
                .zIndex(0f)
            )
        }
        if(viewModel.getIsCircularProgressBarActive()){

            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha)), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }else{

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(enabled = viewModel.getIsEEditDeleteButtonEnabled() != "") {
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
                    items(customersList.sortedBy { it.isSuspended }) { item ->
                        Spacer(modifier = Modifier.height(16.dp))
                        ShowDataView(item, navController, viewModel)
                    }
                }
            }
        }

            if(viewModel.isActionButtonExtended.value){

                BlurredBackground(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha))
                    .clickable { viewModel.isActionButtonExtended.value = false }
                )
            }

    }
    if(viewModel.getIsSetTempAmountViewActive()){
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha))
            .clickable { viewModel.resetHomeViewState() }
        )
    }
    AnimatedVisibility(visible =viewModel.getIsSetTempAmountViewActive(),
        enter = slideInVertically(animationSpec = tween(durationMillis = 100), initialOffsetY = { fullHeight -> fullHeight / 6 })
                + expandVertically(animationSpec = tween(durationMillis = 100),expandFrom = Alignment. Top)
                + scaleIn(animationSpec = tween(durationMillis = 100),transformOrigin = TransformOrigin(0.5f, 0f))
                + fadeIn(animationSpec = tween(durationMillis = 100),initialAlpha = 0.3f),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight / 6 }) + shrinkVertically() + fadeOut() + scaleOut(targetScale = 0.2f)

    ) {

        ChangeQuantityScreen(viewModel)
    }
    if (viewModel.getIsAlertDialogBox().value) {
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha))
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
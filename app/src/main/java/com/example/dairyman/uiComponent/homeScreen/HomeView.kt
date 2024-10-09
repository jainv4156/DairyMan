package com.example.dairyman.uiComponent.homeScreen

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.dairyman.DairyApp
import com.example.dairyman.snackBar.ObserveAsEvent
import com.example.dairyman.snackBar.SnackBarAction
import com.example.dairyman.snackBar.SnackBarController
import com.example.dairyman.snackBar.SnackBarEvent
import com.example.dairyman.viewmodel.HomeViewModel
import com.example.dairyman.uiComponent.AlertDialogBoxView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel,
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
                duration = SnackbarDuration.Short
            )

            if(result == SnackbarResult.ActionPerformed) {
                snackBarHostState.currentSnackbarData?.dismiss()
            }
        }
    }
    val alpha by animateFloatAsState(
        targetValue =if (viewModel.getIsEEditDeleteButtonEnabled()!=""||viewModel.isActionButtonExtended.value||viewModel.getIsSetTempAmountViewActive() ||viewModel.getIsAlertDialogBox()) 0.5f else 0f, label = "",
        animationSpec = tween(250)
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
        var backPressCount by remember { mutableIntStateOf(0) }
        val activity = LocalContext.current as Activity

        BackHandler {
            if (backPressCount == 0) {
                // Execute your custom function
                viewModel.resetHomeViewState()
                backPressCount++
            } else {
                activity.finish()
                backPressCount = 0
            }
        }
        val myApplication =activity.applicationContext as DairyApp
        if(myApplication.isAppRestart && FirebaseAuth.getInstance().currentUser ==null){
            viewModel.activateSyncRecommendation()
            myApplication.isAppRestart=false
        }
        LaunchedEffect(key1 = Unit) {
            if(myApplication.readLastSyncTime() != 0L){
                val lastSyncTime=myApplication.readLastSyncTime()
                val date= SimpleDateFormat("dd/MM", Locale.getDefault()).format(lastSyncTime)
                val time= SimpleDateFormat("HH:mm", Locale.getDefault()).format(lastSyncTime)
                myApplication.saveAutoSyncUpdates(true)
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = "Last synced: $time on $date",
                        action = SnackBarAction(name = "X")
                    )
                )
            }
        }

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
        }

        if(!viewModel.getIsCircularProgressBarActive() && !viewModel.getIsAlertDialogBox() && !viewModel.getIsSetTempAmountViewActive()){


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
    if(viewModel.getIsSetTempAmountViewActive()||viewModel.getIsAlertDialogBox()){
        BlurredBackground(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha))
            .clickable { viewModel.resetHomeViewState() }
        )
    }
    AnimatedVisibility(visible =viewModel.getIsSetTempAmountViewActive(),
        enter = scaleIn(animationSpec = tween(durationMillis = 250))
                + fadeIn(animationSpec = tween(durationMillis = 250),initialAlpha = 0.3f),
        exit =  scaleOut(animationSpec = tween(durationMillis = 200))
                + fadeOut(animationSpec = tween(durationMillis = 200), targetAlpha = 0.3f)

    ) {

        ChangeQuantityScreen(viewModel)
    }

    AnimatedVisibility(visible =viewModel.getIsAlertDialogBox(),
        enter = slideInVertically(animationSpec = tween(durationMillis = 250),initialOffsetY = { fullHeight -> -fullHeight })
                +scaleIn(animationSpec = tween(durationMillis = 250))
      ,
        exit =  scaleOut(animationSpec = tween(durationMillis = 200))
                + fadeOut(animationSpec = tween(durationMillis = 200), targetAlpha = 0.3f)

    ) {

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
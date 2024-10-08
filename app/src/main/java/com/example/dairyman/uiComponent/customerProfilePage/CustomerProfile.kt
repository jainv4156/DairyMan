package com.example.dairyman.uiComponent.customerProfilePage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.snackBar.ObserveAsEvent
import com.example.dairyman.snackBar.SnackBarController
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.Secondary
import com.example.dairyman.uiComponent.homeScreen.BlurredBackground
import com.example.dairyman.uiComponent.homeScreen.ScreenA
import com.example.dairyman.viewmodel.HomeViewModel
import com.example.dairyman.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerProfileView(id: String,navController: NavController,viewModel:HistoryViewModel) {
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
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        topBar = { CustomerProfileTopBar(id = id, onBackNavClicked = { navController.navigate(
        ScreenA
    ) }) }) {
        val profile= HomeViewModel().getProfileDataForHistory(id).collectAsState(initial = null)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(it),){
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)){
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)) {
                Row {

                Text(
                    text = "Pending Amount:" ,
                    fontSize = 20.sp,

                )
                    Text(
                        text = " Rs" + profile.value?.pendingAmount.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,

                        )
                }
                Row {

                Text(
                    text = "Rate:" ,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top=12.dp)

                )
                    Text(
                        text = " Rs" + profile.value?.rate.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top=12.dp)

                    )
                }
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    if(profile.value !=null){
                        val dateUpdated = simpleDateFormat.format(profile.value!!.dateUpdated)
                        Text(text = "Last updated: $dateUpdated",
                            modifier = Modifier.padding(top=12.dp)
                        )


                    }

                Box(modifier = Modifier
                    .padding(top=12.dp)
                    .clickable {
                        viewModel.setPendingAmountId(id)
                        viewModel.setChangeAmountViewStatus(true)
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Secondary)
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))

                ) {
                    Text(
                        modifier = Modifier.padding(8.dp, 4.dp),
                        text = "Change Amount"
                    )
                }
            }
                Canvas(modifier = Modifier.fillMaxWidth()){
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3f
                    )}
                CustomerHistoryView(id = id)
            }

        }

        val alpha by animateFloatAsState(
            targetValue =if (viewModel.getChangeAmountViewStatus()) 0.5f else 0f, label = "",
            animationSpec = tween(250)
        )
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = alpha))
                .clickable { viewModel.setChangeAmountViewStatus(false) }
            )
        AnimatedVisibility(visible =viewModel.getChangeAmountViewStatus(),
            enter = scaleIn(animationSpec = tween(durationMillis = 250))
                    + fadeIn(animationSpec = tween(durationMillis = 250),initialAlpha = 0.3f),
            exit =  scaleOut(animationSpec = tween(durationMillis = 200))
                    + fadeOut(animationSpec = tween(durationMillis = 200), targetAlpha = 0.3f)

        ) {
        ChangeAmountView(viewModel)
        }
    }
}
@Serializable
data class ScreenC(
    val id:String
)

package com.example.dairyman.uiComponent.homeScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.ui.theme.Secondary
import com.example.dairyman.uiComponent.ScreenB
import com.example.dairyman.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FloatingActionButtonView(
    viewModel: HomeViewModel,
    navController: NavController
){

    Column ( horizontalAlignment = Alignment.End){
//    if(viewModel.isActionButtonExtended.value){
        ActionButton(viewModel,navController)

//    }
        val floatingActionButtonColor by animateColorAsState(
            targetValue =if (viewModel.isActionButtonExtended.value) Primary else  Secondary,
            label = "",
            animationSpec = tween(250)
        )
    FloatingActionButton(
        containerColor= floatingActionButtonColor,
        elevation = FloatingActionButtonDefaults.elevation(6.dp ),
        contentColor = contentColorFor(containerColor),
        onClick = { viewModel.setIsActionButtonExtended(!viewModel.getIsActionButtonExtended()) },

    ) {
        Icon(imageVector = Icons.Default.Add,contentDescription = null, tint = if (viewModel.isActionButtonExtended.value) Color.White else  Color.Black)
    }
    }


}
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun ActionButton(viewModel: HomeViewModel, navController: NavController){
    var animationState by remember {
        mutableStateOf(false)
    }
    animationState=viewModel.getIsActionButtonExtended()
    val scope=rememberCoroutineScope()
    val context:Context= LocalContext.current
    AnimatedVisibility(visible = animationState,
        enter = slideInVertically(animationSpec = tween(durationMillis = 250), initialOffsetY = { fullHeight -> fullHeight })
                + fadeIn(animationSpec = tween(durationMillis = 250),initialAlpha = 0.3f)
                + expandVertically(animationSpec = tween(durationMillis = 250),expandFrom = Alignment.CenterVertically)
                + scaleIn(animationSpec = tween(durationMillis = 250), transformOrigin = TransformOrigin(pivotFractionX = 1f, pivotFractionY = 1f))

        ,
        exit = slideOutVertically (animationSpec = tween(durationMillis = 250), targetOffsetY = { fullHeight -> fullHeight })
                + fadeOut(animationSpec = tween(durationMillis = 250), targetAlpha = 0.3f)
                +  shrinkVertically(animationSpec = tween(durationMillis = 250), shrinkTowards = Alignment.CenterVertically)
                + scaleOut(animationSpec = tween(durationMillis = 250), transformOrigin = TransformOrigin(pivotFractionY = 1f, pivotFractionX =1f ))

    ){
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {

//        AnimatedVisibility(visible = animationState) {

            Box(
                modifier = Modifier

                    .padding(bottom = 16.dp)

                    .clip(RoundedCornerShape(10.dp))
                    .background(Secondary)
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .clickable {
                        scope.launch {
                            viewModel.checkSyncDataWithCloud(context)
                        }
                    }

            ) {
                Text(text = "Sync", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Medium, fontSize = 18.sp)
            }
//        }


            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Secondary)
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                    .clickable { navController.navigate(ScreenB()) }
            ) {
                Text(text = "Add Customer", fontWeight = FontWeight.Medium, modifier = Modifier.padding(10.dp), fontSize = 18.sp)
            }
//        }
//        AnimatedVisibility(visible = animationState) {
            Box(modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Secondary)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .clickable {
                    scope.launch {
                        viewModel.checkTodayUpdate()
                        viewModel.resetHomeViewState()

                    }
                }) {
                Text(text = "Update Today Amount", fontWeight = FontWeight.Medium, modifier = Modifier.padding(10.dp), fontSize = 18.sp)
            }
//        }

    }
}}

package com.example.dairyman.uiComponent.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.ui.theme.Accent
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.DarkBackground
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.ui.theme.Secondary
import com.example.dairyman.uiComponent.customerProfilePage.ScreenC
import com.example.dairyman.viewmodel.HomeViewModel

@Composable
fun ShowDataView(item: DairyData, navController: NavController, viewModel: HomeViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 6.dp)
    ){
        Column(modifier = Modifier

            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(color = Primary)

            .clickable(enabled = viewModel.getIsEEditDeleteButtonEnabled() == "") {
                navController.navigate(
                    ScreenC(id = item.id)
                )
            }
            .padding(16.dp)
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
                        color = Background,
                        text = "Quantity: " + if (item.amount == item.tempAmount) item.tempAmount.toString() + "kg" else item.tempAmount.toString() + "Kg" + " for " + item.dayForTempAmount + if (item.dayForTempAmount == 1) " Day" else " Days"
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier

                        .clip(RoundedCornerShape(10.dp))
                        .clickable(enabled = !item.isSuspended) {
                            viewModel.setIdTempAmount(id = item.id)
                            viewModel.readySetTempAmountView()
                        }
                        .background(color = if (!item.isSuspended) Secondary else DarkBackground)


                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp, 4.dp),
                            text = "Change"
                        )
                    }
                    Icon(modifier = Modifier
                        .clickable { viewModel.enableMoreOption(item.id) }
                        .padding(start = 8.dp),
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "MoreOptions",
                        tint = Accent
                    )
                }
            }
        }
        if(viewModel.getIsEEditDeleteButtonEnabled() != ""){
            BlurredBackground(modifier = Modifier
                .fillMaxSize()
                .clickable { viewModel.resetHomeViewState() }
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black.copy(alpha = 0.5f))
                .height(88.dp)

            )
        }
        AnimatedVisibility(
            visible =viewModel.getIsEEditDeleteButtonEnabled()==item.id,
            enter = fadeIn(animationSpec = tween(durationMillis = 250),initialAlpha = 0.3f)
            + expandVertically (animationSpec = tween(durationMillis = 250), initialHeight ={fullHeight -> fullHeight/8})
            + scaleIn(animationSpec = tween(durationMillis = 250), initialScale = 0.2f, transformOrigin = TransformOrigin(pivotFractionX = 0.75f, pivotFractionY =0.2f )),
            exit = fadeOut(animationSpec = tween(durationMillis = 250), targetAlpha = 0.3f)
                    + shrinkVertically (animationSpec = tween(durationMillis = 250), targetHeight ={fullHeight -> fullHeight/8})
                    + scaleOut(animationSpec = tween(durationMillis = 250), targetScale = 0.2f, transformOrigin = TransformOrigin(pivotFractionX = 0.75f, pivotFractionY =0.2f )), ){
            MoreOptionView(
                navController = navController, item = item,viewModel=viewModel)
        }
    }
}
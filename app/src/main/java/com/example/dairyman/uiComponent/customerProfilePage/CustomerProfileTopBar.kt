package com.example.dairyman.uiComponent.customerProfilePage

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.viewmodel.HistoryViewModel
import com.example.dairyman.viewmodel.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileTopBar(id:String,onBackNavClicked:()->Unit={},viewModel:HistoryViewModel){
    val navigationIcon: @Composable () ->Unit = {
        IconButton(onClick = {
            if(!viewModel.getChangeAmountViewStatus()){
                onBackNavClicked()

            }
            else{
                viewModel.setChangeAmountViewStatus(false)
            }
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = Background,
                contentDescription = null)
        }
    }
    val profile= HomeViewModel().getProfileDataForHistory(id).collectAsState(initial = null)
    TopAppBar(
        modifier = Modifier.clip(RoundedCornerShape(0.dp,0.dp,16.dp,16.dp)),
        title = {
            profile.value?.let {
                Text(
                    text = it.name,
                    color = Background,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
        },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary
        ),

        )
}

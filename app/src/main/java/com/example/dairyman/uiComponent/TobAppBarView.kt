package com.example.dairyman.uiComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.dairyman.R
import com.example.dairyman.ui.theme.Accent
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.Primary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarView(title:String,onBackNavClicked:()->Unit={}){
    val navigationIcon: @Composable () ->Unit = {
            IconButton(onClick = { onBackNavClicked()}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Background,
                    contentDescription = null)
            }
    }
        TopAppBar(
            modifier = Modifier.clip(RoundedCornerShape(0.dp,0.dp,16.dp,16.dp)),
            title = {
                Text(
                    text = title,
                    color = Background,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            },
            navigationIcon = navigationIcon,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Primary
            ),
            actions = { IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier =Modifier.size(32.dp),
                    imageVector = Icons.Default.Search,
                    tint = Accent,
                    contentDescription = null)
            } }
        )
    }

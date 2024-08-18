package com.example.dairyman.Ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.dairyman.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarView(title:String,onBackNavClicked:()->Unit={}){
    val colorOnThemeBase=  if(isSystemInDarkTheme()) colorResource(R.color.white) else colorResource(R.color.black)
    val navigationIcon: @Composable () ->Unit = {
            IconButton(onClick = { onBackNavClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = colorOnThemeBase,
                    contentDescription = null)
            }

    }
    TopAppBar(
        title = {
            Text(text = title,
                color = colorOnThemeBase,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)) }
        ,
        navigationIcon = navigationIcon,
    )
}

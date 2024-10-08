package com.example.dairyman.uiComponent.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.uiComponent.ProfilePhoto
import com.example.dairyman.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopView(viewModel: HomeViewModel, title:String, navController: NavController,
){
    val navigationIcon: @Composable () ->Unit = {
        if(viewModel.getIsSearchActive()){
            IconButton(onClick = {viewModel.resetHomeViewState() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Background,
                    contentDescription = null)
            }
        }
    }
    val trailingIcon:@Composable () ->Unit ={
        if(viewModel.getSearchQuery().isNotEmpty()){
            IconButton(onClick = {viewModel.setSearchQuery("") }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = Background,
                    contentDescription = null)
            }
        }
    }
    @Composable
    fun searchView(){
        val focusRequester = remember {
            FocusRequester()
        }

        if(viewModel.getIsSearchActive()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){

            Spacer(Modifier.weight(3f))
            TextField(
                value = viewModel.getSearchQuery(),
                onValueChange = {
                    viewModel.setSearchQuery(it)
                },
                placeholder = {Text("Search", color = Background)},

                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .weight(20f)
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions.Default,
                trailingIcon = trailingIcon,
                textStyle = TextStyle(color = Background, fontSize = 20.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Background,
                    unfocusedBorderColor = Background,
                    focusedLabelColor = Background,
                    unfocusedLabelColor = Background,
                    cursorColor = Background,
                ),
            )
            Spacer(Modifier.weight(1f))
            LaunchedEffect(key1 = Unit) {
                focusRequester.requestFocus()
            }

        }
        }

    }

    TopAppBar(

        modifier = Modifier.clip(RoundedCornerShape(0.dp,0.dp,16.dp,16.dp)),
        navigationIcon = navigationIcon,
        title = {
            if(!viewModel.getIsSearchActive()) {
                Text(
                    text = title,
                    color = Background,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Primary
        ),

        actions = {

            AnimatedVisibility(visible = viewModel.getIsSearchActive(),
                enter = fadeIn(animationSpec = tween(durationMillis = 500),initialAlpha = 0.3f)
                + expandHorizontally(animationSpec = tween(durationMillis = 500), expandFrom = Alignment.End),){
                searchView()
            }
            if(!viewModel.getIsSearchActive())
                IconButton(onClick = {

                    viewModel.enableSearch() }) {
                    Icon(
                        modifier =Modifier.size(32.dp),
                        imageVector = Icons.Default.Search,
                        tint = Background,
                        contentDescription = null)
                }
                ProfilePhoto( navController = navController)
        }
    )

}

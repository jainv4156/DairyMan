package com.example.dairyman.uiComponent.HomeScreen


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dairyman.ui.theme.Accent
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.Primary
import com.example.dairyman.uiComponent.ProfilePhoto
import com.example.dairyman.viewmodel.DairyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopView(viewModel: DairyViewModel, title:String, navController: NavController,
){
    val navigationIcon: @Composable () ->Unit = {
        if(viewModel.getIsSearchActive()){
            IconButton(onClick = {viewModel.disableSearch() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = Background,
                    contentDescription = null)
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
            if(viewModel.getIsSearchActive()) {
                Spacer(Modifier.weight(1f))
                OutlinedTextField(
                    value = viewModel.getSearchQuery(),
                    onValueChange = {
                        viewModel.setSearchQuery(it)
                    },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            else{
                IconButton(onClick = {
                    viewModel.enableSearch() }) {
                    Icon(
                        modifier =Modifier.size(32.dp),
                        imageVector = Icons.Default.Search,
                        tint = Accent,
                        contentDescription = null)
                } }
                ProfilePhoto(viewModel = viewModel, navController = navController)
        }
    )

}

package com.example.dairyman.uiComponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.viewmodel.DairyViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ProfilePhoto(viewModel: DairyViewModel, navController: NavController){
    if(FirebaseAuth.getInstance().currentUser?.photoUrl==null){
        IconButton(
            onClick = {navController.navigate(ScreenD)}) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.AccountCircle,
                tint = Background,
                contentDescription = null)
        }
    }else {
        AsyncImage(
            model = FirebaseAuth.getInstance().currentUser?.photoUrl,
            contentDescription = "Photo",
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .clickable { navController.navigate(ScreenD) },
            contentScale = ContentScale.Crop
        )
    }

}
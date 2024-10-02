package com.example.dairyman.uiComponent.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dairyman.ui.theme.DarkBackground

@Composable
fun NavigationDrawerView(){

ModalDrawerSheet {
    Box(modifier = Modifier
        .shadow(
            elevation = 3.dp,
            RoundedCornerShape(12.dp),
            ambientColor = Color.Black,
            spotColor = Color.Black
        )
        .padding(1.dp, 0.dp, 1.dp, 5.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(color = DarkBackground)
        .padding(16.dp, 4.dp)
        .clickable {
//                viewModel.deleteDataById(item)
        }
    ) {
        Text(fontWeight = FontWeight.Medium,text = "delete")
    }
}
}
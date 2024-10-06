package com.example.dairyman.uiComponent.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.dairyman.data.model.DairyData
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.DarkBackground
import com.example.dairyman.uiComponent.ScreenB
import com.example.dairyman.viewmodel.DairyViewModel

@Composable
fun MoreOptionView(
    navController: NavController,
    item: DairyData,
    viewModel: DairyViewModel
){
    Column (horizontalAlignment = Alignment.End ,
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)

    ){
        Column (modifier = Modifier
            .padding(0.dp, 24.dp, 34.dp, 0.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Background)
            .padding(16.dp)
        ){
            Box (modifier = Modifier

                .shadow(
                    elevation = 3.dp,
                    RoundedCornerShape(12.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .padding(1.dp, 0.dp, 1.dp, 5.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color = DarkBackground)
                .clickable {
                    navController.navigate(ScreenB(item.id))
                }
                .padding(16.dp, 4.dp)

            ){
                Text(fontWeight = FontWeight.Medium,text = "edit")
            }
            Spacer(modifier = Modifier.height(12.dp))
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
                .clickable {
                    viewModel.enableDeleteAlert(item)
                }
                .padding(16.dp, 4.dp)


            ) {
                Text(fontWeight = FontWeight.Medium,text = "delete")
            }

        }
    }
}
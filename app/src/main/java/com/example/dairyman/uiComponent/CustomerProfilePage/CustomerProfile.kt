package com.example.dairyman.uiComponent.CustomerProfilePage

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.uiComponent.HomeScreen.ScreenA
import com.example.dairyman.uiComponent.TopAppBarView
import com.example.dairyman.viewmodel.DairyViewModel
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerProfileView(id: Long,navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { CustomerProfileTopBar(title = id.toString(), onBackNavClicked = { navController.navigate(
        ScreenA
    ) }) }) {
        val history= DairyViewModel().getHistoryById(id).collectAsState(initial = listOf())
        val profile= DairyViewModel().getProfileDataForHistory(id).collectAsState(initial = null)
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)) {
                if (profile.value != null && history.value.isNotEmpty()) {
                    val shape = RoundedCornerShape(8.dp)
                    Column(
                        modifier = Modifier
                            .shadow(
                                5.dp,
                                shape,
                                spotColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                ambientColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                            )
                            .background(if (isSystemInDarkTheme()) DarkGray else LightGray)
                    )
                    {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
//                                Text(
//                                    text = (profile.value?.name?.substring(0, 1)
//                                        ?.uppercase()) + history.value[0].substring(1)
//                                        .lowercase(),
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
                                Text(
                                    text = profile.value?.rate.toString() + "Rs",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                val simpleDateFormat =
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                                val dateUpdated =
                                    simpleDateFormat.format(profile.value?.dateUpdated)
                                Text(text = "Last updated on $dateUpdated")
                            }

                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Rate", fontSize = 20.sp)
                        Text(text = "Date", fontSize = 20.sp)
                    }
                }

                CustomerHistoryView(id = id)
            }
        }
    }
}
@Serializable
data class ScreenC(
    val id:Long
)

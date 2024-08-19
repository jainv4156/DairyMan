package com.example.dairyman.uiComponent

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
import com.example.dairyman.DairyViewModel
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DairyHistoryVIew(id: Long,navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopAppBarView(title = "History", onBackNavClicked = { navController.navigate(ScreenA) }) }) {
        val history= DairyViewModel().getHistoryById(id).collectAsState(initial = listOf())
        val profile= DairyViewModel().getProfileDataForHistory(id).collectAsState(initial = null)
        val subContentColor=if(isSystemInDarkTheme()) LightGray else DarkGray
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)) {
                if(profile.value!=null){
                    val shape = RoundedCornerShape(8.dp)
                        Column (modifier = Modifier
                            .shadow(
                                5.dp,
                                shape,
                                spotColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                ambientColor =if (isSystemInDarkTheme()) Color.White else Color.Black
                            )
                            .background(if (isSystemInDarkTheme()) DarkGray else LightGray))
                        {
                            Column(modifier = Modifier.padding(16.dp)){


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = (profile.value?.name?.substring(0, 1)
                                    ?.uppercase()) + history.value[0].name.substring(1).lowercase(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            Text(
                                text = profile.value?.rate.toString() + "Rs",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            val simpleDateFormat=SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val dateCreated=simpleDateFormat.format(profile.value?.dateCreated)
                            val dateUpdated=simpleDateFormat.format(profile.value?.dateUpdated)
                            Text(text = "Created on : $dateCreated", color = subContentColor)
                            Text(text = "Last updated on $dateUpdated", color = subContentColor)
                        }

                    }
                }
                }
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), horizontalArrangement =Arrangement.SpaceBetween){
                    Text(text ="Rate", fontSize = 20.sp)
                    Text(text = "Date", fontSize = 20.sp)
                }
                LazyColumn (modifier = Modifier.fillMaxWidth()){
                    items(history.value){item->

                        val simpleDate=SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val date=simpleDate.format(item.Date)
                        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement =Arrangement.SpaceBetween){
                            Text(text = item.tempAmount.toString())
                            Text(text = date)
                        }
                    }
                }

        }


        }
    }
    }
@Serializable
data class ScreenC(
    val id:Long
)

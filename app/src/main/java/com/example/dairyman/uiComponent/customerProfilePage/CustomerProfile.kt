package com.example.dairyman.uiComponent.customerProfilePage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.uiComponent.homeScreen.ScreenA
import com.example.dairyman.viewmodel.DairyViewModel
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CustomerProfileView(id: String,navController: NavController) {
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = { CustomerProfileTopBar(id = id, onBackNavClicked = { navController.navigate(
        ScreenA
    ) }) }) {
        val profile= DairyViewModel().getProfileDataForHistory(id).collectAsState(initial = null)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(it),){
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)){
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)) {
                Row {

                Text(
                    text = "Pending Amount:" ,
                    fontSize = 20.sp,

                )
                    Text(
                        text = " Rs" + profile.value?.pendingAmount.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,

                        )
                }
                Row {

                Text(
                    text = "Rate:" ,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top=12.dp)

                )
                    Text(
                        text = " Rs" + profile.value?.rate.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top=12.dp)

                    )
                }

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                if(profile.value !=null){
                    val dateUpdated = simpleDateFormat.format(profile.value!!.dateUpdated)
                    Text(text = "Last updated: $dateUpdated",
                        modifier = Modifier.padding(top=12.dp)
                        )


                }
            }
                Canvas(modifier = Modifier.fillMaxWidth()){
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3f
                    )}
                CustomerHistoryView(id = id)
            }

        }
    }
}
@Serializable
data class ScreenC(
    val id:String
)

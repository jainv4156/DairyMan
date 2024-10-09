package com.example.dairyman.uiComponent.customerProfilePage

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dairyman.ui.theme.Background
import com.example.dairyman.ui.theme.DarkBackground
import com.example.dairyman.uiComponent.OutlinedTextFieldStyle
import com.example.dairyman.viewmodel.HistoryViewModel
import kotlinx.coroutines.launch


@Composable
fun ChangeAmountView(viewModel: HistoryViewModel){
    Box (modifier = Modifier
        .fillMaxSize()
        .padding(32.dp, 0.dp, 32.dp, 32.dp),
    contentAlignment = Alignment.Center){
        val context:Context= LocalContext.current
        val scope= rememberCoroutineScope()
        Column (modifier = Modifier
            .align(Alignment.Center)
            .clip(RoundedCornerShape(8.dp))
            .background(Background)
            .padding(32.dp)
            .pointerInput(Unit){
                detectTapGestures {
                }
            },
            horizontalAlignment = Alignment.CenterHorizontally){
            OutlinedTextFieldStyle(value = viewModel.getPendingAmount(), onValueChange = {viewModel.setPendingAmount(it)}, title = "Amount")
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
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
                    .padding(16.dp, 4.dp)
                    .clickable {
                        if(viewModel.getPendingAmount().isNotEmpty()){
                            scope.launch {
                                viewModel.subtractFromPendingAmount()
                            }
                        }
                        else{
                            Toast.makeText(context,"Please Fill Required The Fields", Toast.LENGTH_SHORT).show()
                        }
                    }
                ){
                    Text(fontWeight = FontWeight.Medium,text = "Subtract")
                }
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
                        if(viewModel.getPendingAmount().isNotEmpty()){
                            scope.launch {
                                viewModel.addInPendingAmount()
                            }
                        }
                        else{
                            Toast.makeText(context,"Please Fill Required The Fields", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .padding(16.dp, 4.dp)
                ){
                    Text(fontWeight = FontWeight.Medium,text = "Add")
                }
            }
}}}
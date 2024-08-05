package com.example.dairyman

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Composable
fun StartingView(navController: NavController) {

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = { FloatingActionButton {
        navController.navigate(
            ScreenB
        )
    }
    } ) { it ->
        Row(modifier = Modifier.fillMaxSize()) {
            Button(onClick = { navController.navigate(ScreenB) }, modifier = Modifier.padding(it)) {
                Text(text = "ClickME1")
            }
        }
    }
}
@Serializable
object ScreenA
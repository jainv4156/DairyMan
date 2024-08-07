package com.example.dairyman

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun FloatingActionButton(onclick: ()-> Unit ){
    FloatingActionButton(onClick = { onclick() }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }

}

package com.example.dairyman.uiComponent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun OutlinedTextFieldStyle(
    value:String,
    onValueChange:(String)-> Unit,
    title:String,
    modifier:Modifier=Modifier,
    keyboardOptions:KeyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Decimal),
    keyboardAction: KeyboardActions = KeyboardActions.Default,
){


    OutlinedTextField(
        label = { Text(text = title) },
        value = value,
        onValueChange = onValueChange,
        modifier =Modifier.let { modifier.fillMaxWidth() },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardAction,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            cursorColor = Color.Black
        )
    )

}
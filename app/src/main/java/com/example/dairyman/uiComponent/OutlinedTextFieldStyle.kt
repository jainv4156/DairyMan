package com.example.dairyman.uiComponent

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.dairyman.R

@Composable
fun OutlinedTextFieldStyle(
    value:String,
    onValueChange:(String)-> Unit,
    title:String,
    keyboardOptions:KeyboardType= KeyboardType.Decimal
){
    val colorOnThemeBase=  if(isSystemInDarkTheme()) colorResource(R.color.white) else colorResource(
        R.color.black)

    OutlinedTextField(
        label = { Text(text = title) },
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType =keyboardOptions),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorOnThemeBase,
            unfocusedBorderColor = colorOnThemeBase,
            focusedLabelColor = colorOnThemeBase,
            unfocusedLabelColor = colorOnThemeBase,
            cursorColor = colorOnThemeBase
        )
    )

}
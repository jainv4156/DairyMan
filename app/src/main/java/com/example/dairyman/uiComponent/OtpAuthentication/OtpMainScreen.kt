package com.example.dairyman.uiComponent.OtpAuthentication

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dairyman.Data.Model.userdataModel.SignInState
//import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.serialization.Serializable

@Composable
fun SignInScreen(
    state:SignInState,
    onSignInClick:()-> Unit
){
    val context= LocalContext.current
    LaunchedEffect(key1 = state.signInError, block = {
        state.signInError?.let { error->
            Toast.makeText(context, error,Toast.LENGTH_LONG).show()
        }
    })

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        Button(onClick =  onSignInClick) {
            Text(text = "SignIn")
        }
    }
}
@Serializable
object ScreenD
package com.example.dairyman.uiComponent.OtpAuthentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dairyman.Data.Model.userdataModel.SignInState
import com.example.dairyman.uiComponent.ScreenA
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.oAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
//import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

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
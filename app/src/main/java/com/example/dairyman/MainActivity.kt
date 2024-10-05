package com.example.dairyman

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.ModalNavigationDrawer
import com.example.dairyman.Presentation.Sign_in.GoogleAuthUiClint
import com.example.dairyman.ui.theme.DairyManTheme
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClint(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DairyManTheme {

                    Navigation(googleAuthUiClient=googleAuthUiClient)
            }
        }
    }
}


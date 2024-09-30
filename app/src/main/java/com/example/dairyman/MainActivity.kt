package com.example.dairyman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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


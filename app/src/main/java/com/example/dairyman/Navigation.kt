package com.example.dairyman

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.dairyman.Presentation.Sign_in.GoogleAuthUiClint
import com.example.dairyman.viewmodel.AddUpdateCustomerDetailViewModel
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.uiComponent.AddUpdateCustomerDetailView
import com.example.dairyman.uiComponent.DairyHistoryVIew
import com.example.dairyman.uiComponent.OtpAuthentication.ScreenD
import com.example.dairyman.uiComponent.OtpAuthentication.SignInScreen
import com.example.dairyman.uiComponent.ScreenA
import com.example.dairyman.uiComponent.ScreenB
import com.example.dairyman.uiComponent.ScreenC
import com.example.dairyman.uiComponent.StartingView
import com.example.dairyman.viewmodel.SignInViewModel
import kotlinx.coroutines.launch


@Composable
fun Navigation(
    googleAuthUiClient:GoogleAuthUiClint,
navController: NavHostController=rememberNavController()
){
    NavHost(
        navController=navController,
        startDestination = ScreenA
    ){
        composable<ScreenD>{
            val viewModel=SignInViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val scope=rememberCoroutineScope()
            val context:Context=navController.context
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if(result.resultCode == RESULT_OK) {
                        scope.launch {
                            val signInResult = googleAuthUiClient
                                .signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )
            LaunchedEffect(key1 = Unit) {
                if(googleAuthUiClient.getSignInUser() != null) {
                    navController.navigate(ScreenA)
                }
            }
            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if(state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(ScreenA)
                    viewModel.resetState()
                }
            }

            SignInScreen(
                state=state,
                onSignInClick = {
                    scope.launch {
                        val signInIntentSender  =googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }

                }
            )
        }

        composable<ScreenA>{
            val scope=rememberCoroutineScope()

            StartingView(navController, viewModel = DairyViewModel(),
                userData = googleAuthUiClient.getSignInUser(),
                onSignOut = {
                    scope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(
                            navController.context,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()

                        navController.navigate(ScreenD)
                    }
                })
        }
        composable<ScreenB> {
            val args=it.toRoute<ScreenB>()
            AddUpdateCustomerDetailView(navController, viewModel = AddUpdateCustomerDetailViewModel(),args.id)
        }
        composable<ScreenC> {
            val args=it.toRoute<ScreenB>()
            DairyHistoryVIew(args.id,navController)
        }
    }

}

package com.example.dairyman

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.example.dairyman.SnackBar.SnackBarController
import com.example.dairyman.SnackBar.SnackBarEvent
import com.example.dairyman.viewmodel.AddUpdateCustomerDetailViewModel
import com.example.dairyman.viewmodel.DairyViewModel
import com.example.dairyman.uiComponent.AddUpdateCustomerDetailView
import com.example.dairyman.uiComponent.CustomerProfilePage.CustomerProfileView
import com.example.dairyman.uiComponent.ScreenD
import com.example.dairyman.uiComponent.SignInScreen
import com.example.dairyman.uiComponent.HomeScreen.ScreenA
import com.example.dairyman.uiComponent.ScreenB
import com.example.dairyman.uiComponent.CustomerProfilePage.ScreenC
import com.example.dairyman.uiComponent.HomeScreen.HomeView
import com.example.dairyman.viewmodel.SignInViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
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
            LaunchedEffect(key1 = state.isSignInSuccessful) {

                if(state.isSignInSuccessful) {
                    scope.launch {
                        SnackBarController.sendEvent(
                            SnackBarEvent(
                                message = "Sign in successful"
                            )
                        )
                    }
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

                },
                navController = navController,
                onSignOut = {
                    scope.launch {
                        googleAuthUiClient.signOut()
                        navController.navigate(ScreenD)
                        SnackBarController.sendEvent(SnackBarEvent(message = "Signed out"))
                    }
                }

            )
        }
        composable<ScreenA>{
            HomeView(navController, viewModel = DairyViewModel())
        }
        composable<ScreenB> {
            val args=it.toRoute<ScreenB>()
            AddUpdateCustomerDetailView(navController, viewModel = AddUpdateCustomerDetailViewModel(),args.id)
        }
        composable<ScreenC> {
            val args=it.toRoute<ScreenB>()
            CustomerProfileView(args.id,navController)
        }
    }


}

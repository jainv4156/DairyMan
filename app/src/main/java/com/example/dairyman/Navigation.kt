package com.example.dairyman

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable


@Composable
fun Navigation(
navController: NavHostController=rememberNavController()
){
    NavHost(
        navController=navController,
        startDestination =ScreenA
    ){
        composable<ScreenA>{
            StartingView(navController)
        }
        composable<ScreenB> {
            AddNewRecordView(navController, viewModel = DairyViewModel())
        }
    }

}

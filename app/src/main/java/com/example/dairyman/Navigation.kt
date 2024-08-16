package com.example.dairyman

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute


@Composable
fun Navigation(
navController: NavHostController=rememberNavController()
){
    NavHost(
        navController=navController,
        startDestination = ScreenA
    ){
        composable<ScreenA>{
            StartingView(navController, viewModel = DairyViewModel())
        }
        composable<ScreenB> {
            val args=it.toRoute<ScreenB>()
            AddNewRecordView(navController, viewModel = DairyViewModel(),args.id)
        }
        composable<ScreenC> {
            val args=it.toRoute<ScreenB>()
            DairyHistoryVIew(args.id)
        }
    }

}

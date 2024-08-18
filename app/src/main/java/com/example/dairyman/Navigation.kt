package com.example.dairyman

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.dairyman.Ui.AddNewRecordView
import com.example.dairyman.Ui.DairyHistoryVIew
import com.example.dairyman.Ui.ScreenA
import com.example.dairyman.Ui.ScreenB
import com.example.dairyman.Ui.ScreenC
import com.example.dairyman.Ui.StartingView


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

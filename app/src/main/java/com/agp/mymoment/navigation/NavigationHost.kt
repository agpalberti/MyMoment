package com.agp.mymoment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.agp.mymoment.ui.login.RegisterScreen

@Composable
fun NavigationHost(navController: NavHostController, startDestination: String){

    NavHost(navController = navController, startDestination = startDestination){
        composable(Destinations.generico.ruta){
            //screen(navController)
        }
        composable(Destinations.RegisterScreen.ruta){
            RegisterScreen(navController)
        }
    }
}
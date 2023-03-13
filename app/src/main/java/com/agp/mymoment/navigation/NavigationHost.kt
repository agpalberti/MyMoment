package com.agp.mymoment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.agp.mymoment.ui.login.RegisterScreen
import com.agp.mymoment.ui.home.HomeScreen
import com.agp.mymoment.ui.notifications.NotificationScreen
import com.agp.mymoment.ui.profile.ProfileScreen
import com.agp.mymoment.ui.search.SearchScreen
import com.agp.mymoment.ui.upload.UploadScreen

@Composable
fun NavigationHost(navController: NavHostController, startDestination: String){

    NavHost(navController = navController, startDestination = startDestination){

        composable(Destinations.RegisterScreen.ruta){
            RegisterScreen(navController)
        }
        composable(Destinations.HomeScreen.ruta){
            HomeScreen(navController = navController)
        }

        composable(Destinations.NotificationScreen.ruta){
            NotificationScreen(navController = navController)
        }

        composable(Destinations.SearchScreen.ruta){
            SearchScreen(navController = navController)
        }

        composable(Destinations.ProfileScreen.ruta){
            ProfileScreen(navController = navController)
        }

        composable(Destinations.UploadScreen.ruta){
            UploadScreen(navController)
        }
    }
}
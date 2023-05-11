package com.agp.mymoment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.agp.mymoment.ui.login.RegisterScreen
import com.agp.mymoment.ui.home.HomeScreen
import com.agp.mymoment.ui.notifications.NotificationScreen
import com.agp.mymoment.ui.profile.ProfileScreen
import com.agp.mymoment.ui.profile.followers.FollowersScreen
import com.agp.mymoment.ui.search.SearchScreen
import com.agp.mymoment.ui.upload.UploadScreen

@Composable
fun NavigationHost(navController: NavHostController, startDestination: String) {

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Destinations.RegisterScreen.ruta) {
            RegisterScreen(navController)
        }
        composable(Destinations.HomeScreen.ruta) {
            HomeScreen(navController = navController)
        }

        composable(Destinations.NotificationScreen.ruta) {
            NotificationScreen(navController = navController)
        }

        composable(Destinations.SearchScreen.ruta) {
            SearchScreen(navController = navController)
        }

        composable(
            "${Destinations.ProfileScreen.ruta}/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("uid")
                ?.let { ProfileScreen(navController, userUID = it) }
        }

        composable(
            "${Destinations.FollowersScreen.ruta}/{index}/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType }, navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val userUID = backStackEntry.arguments?.getString("uid")
            val index = backStackEntry.arguments?.getInt("index")
                ?.let { userUID?.let { it1 -> FollowersScreen(navController, userUID = it1, index =it ) } }
        }


        composable(Destinations.UploadScreen.ruta) {
            UploadScreen(navController)
        }
    }
}
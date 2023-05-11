package com.agp.mymoment.navigation

import com.agp.mymoment.R

sealed class Destinations(val ruta: String, val icon:Int? = null) {
    object RegisterScreen: Destinations("RegisterScreen")
    object HomeScreen: Destinations("HomeScreen", R.drawable.home)
    object NotificationScreen: Destinations("NotificationScreen",R.drawable.like)
    object UploadScreen: Destinations("UploadScreen",R.drawable.add_circle)
    object SearchScreen: Destinations("SearchScreen",R.drawable.search)
    object ProfileScreen:Destinations("ProfileScreen",R.drawable.account_circle)
    object FollowersScreen:Destinations("FollowersScreen")
}
package com.agp.mymoment.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.agp.mymoment.R
import com.agp.mymoment.navigation.Destinations

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Destinations.HomeScreen,
        Destinations.SearchScreen,
        Destinations.UploadScreen,
        Destinations.NotificationScreen,
        Destinations.ProfileScreen
    )

    BottomAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NoRippleBottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon ?: R.drawable.error),
                        contentDescription = item.ruta,
                        modifier = Modifier.size(30.dp)
                    )
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.secondary,
                selected = currentRoute == item.ruta,
                onClick =  {
                    navController.navigate(item.ruta) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
package com.agp.mymoment.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.agp.mymoment.R
import com.agp.mymoment.model.DBM
import com.agp.mymoment.navigation.Destinations

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Destinations.HomeScreen,
        Destinations.SearchScreen,
        Destinations.UploadScreen,
        //Destinations.NotificationScreen,
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
                selected =
                if (item.ruta != Destinations.ProfileScreen.ruta)
                    currentRoute == item.ruta
                else
                    currentRoute == "ProfileScreen/{uid}",
                onClick = {
                    navController.navigate(
                        if (item.ruta != Destinations.ProfileScreen.ruta)
                            item.ruta
                        else
                            "${item.ruta}/${DBM.getLoggedUserUid()}"
                    ) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = false
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
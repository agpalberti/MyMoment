package com.agp.mymoment.ui.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.navigation.ThemedNavBar

@Composable
fun NotificationScreen(navController: NavHostController){
    NotificationScreenBody(navController)
}

@Composable
@Preview
fun NotificationScreenBody(
    navController: NavHostController? = null,
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {
    ThemedNavBar(navController = navController!!, "Notifications") {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Notificacion")


        }
    }

}
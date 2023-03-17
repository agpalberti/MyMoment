package com.agp.mymoment.ui.home

import android.annotation.SuppressLint
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
import com.agp.mymoment.ui.composables.ThemedNavBar


@Composable
fun HomeScreen(navController: NavHostController) {
    HomeScreenBody(navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Preview
fun HomeScreenBody(
    navController: NavHostController? = null,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    ThemedNavBar(navController = navController!!, topBarContent = {

    }) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Home")

        }
    }
}

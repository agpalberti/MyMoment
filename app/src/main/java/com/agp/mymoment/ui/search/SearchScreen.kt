package com.agp.mymoment.ui.search

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
fun SearchScreen(navController: NavHostController){
    SearchScreenBody(navController)
}

@Composable
@Preview
fun SearchScreenBody(
    navController: NavHostController? = null,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    ThemedNavBar(navController = navController!!, "search") {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Buscar")


        }
    }

}
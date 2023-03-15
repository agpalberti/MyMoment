package com.agp.mymoment.ui.upload

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
fun UploadScreen(navController: NavHostController){
    UploadScreenBody(navController)
}

@Composable
@Preview
fun UploadScreenBody(
    navController: NavHostController? = null,
    viewModel: UploadScreenViewModel = hiltViewModel()
) {

    ThemedNavBar(navController = navController!!, topBarContent = {}) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Subir")
        }
    }
}
package com.agp.mymoment.ui.start

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.ui.login.RegisterScreenViewModel


@Composable
fun StartScreen(navController: NavHostController){
    StartScreenBody(navController)
}

@Composable
@Preview
fun StartScreenBody(navController: NavHostController? = null, viewModel: StartScreenViewModel = hiltViewModel()) {
    Text(text = "Funciona")

    //TODO("Not yet implemented")
}

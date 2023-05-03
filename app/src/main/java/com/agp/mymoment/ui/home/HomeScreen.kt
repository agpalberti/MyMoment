package com.agp.mymoment.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.config.MyPreferences
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.agp.mymoment.ui.theme.DarkThemeLogo
import com.agp.mymoment.ui.theme.getLogoId

@Composable
fun HomeScreen(navController: NavHostController) {
    ThemedNavBar(navController = navController, topBarContent = {

        val context = LocalContext.current
        val myPreferences = MyPreferences(context)
        val theme = myPreferences.accessTheme.collectAsState(initial = null)

        val darkTheme = when (theme.value) {
            "true" -> true
            "false" -> false
            else -> null
        }

        Image(painter = painterResource(getLogoId(darkTheme?: isSystemInDarkTheme())), contentDescription = "logo")

    }) {
        HomeScreenBody(navController)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@Preview
fun HomeScreenBody(
    navController: NavHostController? = null,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home")
    }
}

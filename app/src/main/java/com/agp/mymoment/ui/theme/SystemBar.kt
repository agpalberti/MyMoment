package com.agp.mymoment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.agp.mymoment.config.MyPreferences
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SystemBarColor(backgroundColor: Color) {
    val systemUiController = rememberSystemUiController()

    val context = LocalContext.current
    val myPreferences = MyPreferences(context)
    val theme = myPreferences.accessTheme.collectAsState(initial = null)

    val color = when (theme.value) {
        "true" -> Color.Black
        "false" -> Color.White
        else -> null
    }

    val darkIcons = when (theme.value){
        "true" -> false
        "false" -> true
        else -> null
    }?: !isSystemInDarkTheme()


    SideEffect {
        systemUiController.setStatusBarColor(
            color = color?:backgroundColor,
            darkIcons = darkIcons
        )
    }
}

package com.agp.mymoment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.agp.mymoment.R
import com.agp.mymoment.config.MyPreferences

const val LightThemeLogo = R.drawable.logo_negro
const val DarkThemeLogo = R.drawable.logo_blanco

@Composable
fun getLogoId():Int{

    val context = LocalContext.current
    val myPreferences = MyPreferences(context)
    val theme = myPreferences.accessTheme.collectAsState(initial = null)

    val darkTheme = when (theme.value) {
        "true" -> true
        "false" -> false
        else -> null
    }

    return if (darkTheme?: isSystemInDarkTheme()) DarkThemeLogo else LightThemeLogo
}

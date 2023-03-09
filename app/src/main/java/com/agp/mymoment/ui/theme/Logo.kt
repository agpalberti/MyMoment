package com.agp.mymoment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.agp.mymoment.R

private const val LightThemeLogo = R.drawable.logo_oscuro
private const val DarkThemeLogo = R.drawable.logo_blanco

@Composable
fun logo(darkMode:Boolean = isSystemInDarkTheme()):Int{
    return if (darkMode) DarkThemeLogo else LightThemeLogo
}

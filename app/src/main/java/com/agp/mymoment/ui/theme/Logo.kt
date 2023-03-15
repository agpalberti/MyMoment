package com.agp.mymoment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.agp.mymoment.R

const val LightThemeLogo = R.drawable.logo_negro
const val DarkThemeLogo = R.drawable.logo_blanco

@Composable
fun getLogoId(darkMode:Boolean = isSystemInDarkTheme()):Int{
    return if (darkMode) DarkThemeLogo else LightThemeLogo
}

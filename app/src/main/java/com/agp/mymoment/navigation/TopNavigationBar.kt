package com.agp.mymoment.navigation

import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopNavigationBar(text: String){
    TopAppBar(
        title = {
            Text(text, style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.primary)
        }, backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )
}
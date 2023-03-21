package com.agp.mymoment.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ThemedNavBar(navController: NavController, topBarContent:@Composable RowScope.() -> Unit, content: @Composable (() -> Unit)){
    Scaffold(
        topBar = {
            TopNavigationBar(content = topBarContent)
        },
        bottomBar = { BottomNavigationBar(navController = navController) },
        backgroundColor = MaterialTheme.colors.background
    ) {
        content.invoke()
    }
}
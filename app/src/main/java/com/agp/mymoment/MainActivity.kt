package com.agp.mymoment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.navigation.NavigationHost
import com.agp.mymoment.ui.theme.MyMomentTheme
import com.agp.mymoment.ui.theme.SystemBarColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMomentTheme(darkTheme = true) {
                SystemBarColor(color = MaterialTheme.colors.background)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Start()
                }
            }
        }
    }
}

@Composable
fun Start() {
    NavigationHost(
        navController = rememberNavController(),
        startDestination = Destinations.LoginScreen.ruta
    )
}
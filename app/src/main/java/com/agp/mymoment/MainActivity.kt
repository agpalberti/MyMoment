package com.agp.mymoment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.agp.mymoment.config.GetDeviceConfig
import com.agp.mymoment.config.MyPreferences
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.navigation.NavigationHost
import com.agp.mymoment.ui.theme.MyMomentTheme
import com.agp.mymoment.ui.theme.SystemBarColor
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyPreferences.resources = this.resources

        setContent {
            MyMomentTheme() { Start() }

        }
    }
}

@Composable
fun Start() {
    SystemBarColor(color = MaterialTheme.colors.background)
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        GetDeviceConfig()
        //Chekeo si el usuario ya está iniciado para no llevarlo a la pantalla de login
        //Esto retorna null si el usuario no está logeado
        val user = FirebaseAuth.getInstance().currentUser
        var startDestination = ""
        startDestination = if (user == null) {
            Destinations.RegisterScreen.ruta
        } else Destinations.HomeScreen.ruta
        NavigationHost(rememberNavController(), startDestination)
    }
}

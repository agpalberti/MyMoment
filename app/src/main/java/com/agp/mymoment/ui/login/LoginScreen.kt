package com.agp.mymoment.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.ui.theme.logo


@SuppressLint("RestrictedApi")
@Composable
@Preview
fun LoginScreen(navController: NavHostController? = null) {

    var user by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        Box(
            Modifier
                .fillMaxSize()
        ) {
            Image(painterResource(id = logo()), "Logo")

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = user,
                onValueChange = { user = it },
                label = { Text(text = stringResource(R.string.login_usuario)) })

        }
    }
}

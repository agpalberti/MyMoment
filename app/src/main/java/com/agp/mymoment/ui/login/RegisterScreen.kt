package com.agp.mymoment.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.ui.theme.getLogoId

@Composable
@Preview
fun RegisterScreen(navController: NavHostController? = null, viewModel: RegisterScreenViewModel = hiltViewModel()) {

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        
        Row() {
            Spacer(modifier = Modifier.height(75.dp))
        }

        Row {
            Image(painterResource(id = getLogoId(darkMode = true)), "Logo")
        }

        Row() {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = stringResource(id = R.string.eslogan),
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                Divider(color = MaterialTheme.colors.primary, thickness = 1.dp)
            }
        }


        if (viewModel.register){
            Row() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 35.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = stringResource(id = R.string.registro),
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ThemedTextField(
                        value = viewModel.correo,
                        onValueChange = { viewModel.correo = it },
                        placeholder = stringResource(id = R.string.correo),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))

                    Spacer(modifier = Modifier.height(6.dp))

                    ThemedTextField(
                        value = viewModel.nombre ,
                        onValueChange = { viewModel.nombre = it },
                        placeholder = stringResource(id = R.string.nombre_completo),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ThemedTextField(
                        value = viewModel.user,
                        onValueChange = { viewModel.user = it },
                        placeholder = stringResource(id = R.string.nombre_usuario),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        singleLine = true,
                        placeholder = { Text(stringResource(id = R.string.password)) },
                        visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            val image = if (viewModel.passwordVisible)
                                R.drawable.visibility
                            else R.drawable.visibility_off
                            val descripcion = if (viewModel.passwordVisible) stringResource(id = R.string.hide_password) else stringResource(
                                id = R.string.show_password
                            )

                            IconButton(onClick = {viewModel.passwordVisible = !viewModel.passwordVisible}){
                                Icon(imageVector  = ImageVector.vectorResource(id = image), descripcion, modifier = Modifier.size(25.dp))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(onClick = { viewModel.register = !viewModel.register}) {
                        Text(text = stringResource(id = R.string.login_now), textAlign = TextAlign.Center, color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.body1)
                    }

                    Spacer(modifier = Modifier.height(6.dp))


                    Button(onClick = { /*TODO*/ }, modifier = Modifier) {
                        Text(text = stringResource(id = R.string.next),style = MaterialTheme.typography.button)
                    }


                }
            }
        } else {
            Row() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 35.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(90.dp))

                    Text(
                        text = stringResource(id = R.string.login),
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ThemedTextField(
                        value = viewModel.user,
                        onValueChange = { viewModel.user = it },
                        placeholder = stringResource(id = R.string.nombre_usuario),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        singleLine = true,
                        placeholder = { Text(stringResource(id = R.string.password)) },
                        visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            val image = if (viewModel.passwordVisible)
                                R.drawable.visibility
                            else R.drawable.visibility_off
                            val descripcion = if (viewModel.passwordVisible) stringResource(id = R.string.hide_password) else stringResource(
                                id = R.string.show_password
                            )

                            IconButton(onClick = {viewModel.passwordVisible = !viewModel.passwordVisible}){
                                Icon(imageVector  = ImageVector.vectorResource(id = image), descripcion, modifier = Modifier.size(25.dp))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(onClick = { viewModel.register = !viewModel. register}) {
                        Text(text = stringResource(id = R.string.register_now), textAlign = TextAlign.Center, color = MaterialTheme.colors.primaryVariant,
                            style = MaterialTheme.typography.body1)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(onClick = { /*TODO*/ }, modifier = Modifier) {
                        Text(text = stringResource(id = R.string.next), style = MaterialTheme.typography.button)
                    }


                }
            }
        }

    }
}

@Composable
fun ThemedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyBoardOptions: KeyboardOptions,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(text = placeholder)},
        keyboardOptions = keyBoardOptions,
        modifier = Modifier.fillMaxWidth()
    )
}
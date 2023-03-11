package com.agp.mymoment.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.ui.theme.getLogoId

@Composable
@Preview
fun RegisterScreen(
    navController: NavHostController? = null,
    viewModel: RegisterScreenViewModel = hiltViewModel()
) {

    @Composable
    fun TextSwitchButton(text: String) {
        TextButton(onClick = { viewModel.isOnRegister = !viewModel.isOnRegister }) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.body1
            )
        }
    }

    @Composable
    fun TextHeader(text: String) {
        Text(
            text = text,
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun PasswordTextField() {
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
                val descripcion =
                    if (viewModel.passwordVisible) stringResource(id = R.string.hide_password) else stringResource(
                        id = R.string.show_password
                    )

                IconButton(onClick = { viewModel.passwordVisible = !viewModel.passwordVisible }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = image),
                        descripcion,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        )
    }

    @Composable
    fun EmailTextField() {
        ThemedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            placeholder = stringResource(id = R.string.correo),
            keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
    }

    @Composable
    fun NextButton(onClick: () -> Unit) {
        Button(onClick = onClick, modifier = Modifier) {
            Text(text = stringResource(id = R.string.next), style = MaterialTheme.typography.button)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        //region Logo
        Row() {
            Spacer(modifier = Modifier.height(75.dp))
        }

        Row {
            Image(painterResource(id = getLogoId()), "Logo")
        }
        //endregion

        //region Eslogan

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

        //endregion

        if (viewModel.isOnRegister) {
            //region Registro
            Row() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 35.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(25.dp))

                    TextHeader(text = stringResource(id = R.string.registro))

                    Spacer(modifier = Modifier.height(20.dp))

                    EmailTextField()

                    Spacer(modifier = Modifier.height(6.dp))

                    ThemedTextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.name = it },
                        placeholder = stringResource(id = R.string.nombre_completo),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ThemedTextField(
                        value = viewModel.nickname,
                        onValueChange = { viewModel.nickname = it },
                        placeholder = stringResource(id = R.string.nombre_usuario),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PasswordTextField()

                    Spacer(modifier = Modifier.height(6.dp))

                    TextSwitchButton(text = stringResource(id = R.string.login_now))

                    Spacer(modifier = Modifier.height(6.dp))

                    NextButton { viewModel.register() }

                }
            }
            //endregion
        } else {
            //region Login
            Row() {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 35.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(90.dp))

                    TextHeader(text = stringResource(id = R.string.login))

                    Spacer(modifier = Modifier.height(20.dp))

                    EmailTextField()

                    Spacer(modifier = Modifier.height(6.dp))

                    PasswordTextField()

                    Spacer(modifier = Modifier.height(6.dp))

                    TextSwitchButton(stringResource(id = R.string.register_now))

                    Spacer(modifier = Modifier.height(6.dp))

                    NextButton { viewModel.login() }

                }
            }
            //endregion
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
        placeholder = { Text(text = placeholder) },
        keyboardOptions = keyBoardOptions,
        modifier = Modifier.fillMaxWidth()
    )
}
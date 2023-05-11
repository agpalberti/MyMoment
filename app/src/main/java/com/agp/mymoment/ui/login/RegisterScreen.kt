package com.agp.mymoment.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.agp.mymoment.R
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.ui.composables.NoRippleTextButton
import com.agp.mymoment.ui.composables.ThemedTextField
import com.agp.mymoment.ui.theme.getLogoId


@Composable
fun RegisterScreen(navController: NavHostController){
    LazyColumn(Modifier.fillMaxSize()){
    item { RegisterScreenBody(navController)}}
}

@Composable
@Preview
fun RegisterScreenBody(
    navController: NavHostController? = null,
    viewModel: RegisterScreenViewModel = hiltViewModel()
) {

    @Composable
    fun ErrorTooltip(){
        Text(text = viewModel.getErrorString(), color = MaterialTheme.colors.error, style = MaterialTheme.typography.body1, fontSize = 12.sp)
    }

    @Composable
    fun TextSwitchButton(text: String) {

        NoRippleTextButton(onClick = {
            viewModel.resetErrors()
            viewModel.isOnRegister = !viewModel.isOnRegister }) {
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
            isError = viewModel.passwordError.isNotEmpty(),
            singleLine = true,
            placeholder = { Text(stringResource(id = R.string.password)) },
            visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image = if (viewModel.passwordVisible)
                    R.drawable.visibility_off
                else R.drawable.visibility
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
    fun PasswordConfirmationTextField() {
        OutlinedTextField(
            value = viewModel.passwordConfirmation,
            onValueChange = { viewModel.passwordConfirmation = it },
            isError = viewModel.passwordError.isNotEmpty(),
            singleLine = true,
            placeholder = { Text(stringResource(id = R.string.confirmPassword)) },
            visualTransformation = if (viewModel.passwordConfirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image = if (viewModel.passwordConfirmationVisible)
                    R.drawable.visibility_off
                else R.drawable.visibility
                val descripcion =
                    if (viewModel.passwordConfirmationVisible) stringResource(id = R.string.hide_password) else stringResource(
                        id = R.string.show_password
                    )
                IconButton(onClick = { viewModel.passwordConfirmationVisible = !viewModel.passwordConfirmationVisible }) {
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
            labelText = stringResource(id = R.string.correo),
            keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = viewModel.emailError.isNotEmpty()
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

                    //todo enter para cambiar de textfield
                    Spacer(modifier = Modifier.height(20.dp))

                    EmailTextField()

                    Spacer(modifier = Modifier.height(6.dp))

                    ThemedTextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.name = it },
                        labelText = stringResource(id = R.string.nombre_completo),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = viewModel.nameError.isNotEmpty()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    ThemedTextField(
                        value = viewModel.nickname,
                        onValueChange = { viewModel.nickname = it },
                        labelText = stringResource(id = R.string.nombre_usuario),
                        keyBoardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        isError = viewModel.nicknameError.isNotEmpty()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PasswordTextField()

                    Spacer(modifier = Modifier.height(6.dp))

                    PasswordConfirmationTextField()

                    ErrorTooltip()

                    Spacer(modifier = Modifier.height(6.dp))

                    TextSwitchButton(text = stringResource(id = R.string.login_now))

                    Spacer(modifier = Modifier.height(6.dp))

                    NextButton {
                        viewModel.resetErrors()
                        viewModel.register {registered ->
                            if (registered) viewModel.login { logged ->
                                if (logged) {
                                    navController?.navigate(Destinations.HomeScreen.ruta){
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                    }
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

                    ErrorTooltip()

                    Spacer(modifier = Modifier.height(6.dp))

                    TextSwitchButton(stringResource(id = R.string.register_now))

                    Spacer(modifier = Modifier.height(6.dp))

                    NextButton {
                        viewModel.resetErrors()
                        viewModel.login { logged ->
                            if (logged) {
                                navController?.navigate(Destinations.HomeScreen.ruta){
                                    navController.popBackStack()
                                }
                            }
                        }
                    }

                }
            }
            //endregion
        }

    }

}


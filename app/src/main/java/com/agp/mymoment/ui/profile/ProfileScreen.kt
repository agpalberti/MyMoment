package com.agp.mymoment.ui.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.agp.mymoment.R
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.ui.composables.*

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {

    viewModel.updateUserData(viewModel.getActualUserUid())

    Box() {

        ThemedNavBar(navController = navController, topBarContent = {
            Row(Modifier.fillMaxWidth(0.5f), Arrangement.Start) {
                Text(
                    viewModel.userData.nickname ?: "",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary
                )
            }
            Row(Modifier.fillMaxWidth(), Arrangement.End) {
                if (viewModel.getActualUserUid() == viewModel.getActualUserUid()) {
                    NoRippleIconButton(
                        painterResource(id = R.drawable.menu_open),
                        contentDescription = stringResource(id = R.string.open_menu),
                        modifier = Modifier.size(30.dp)
                    ) {
                        //onClick
                        viewModel.turnSidebarMenu()
                    }
                }
            }
        }) {
            ProfileScreenBody(navController)
        }

        if (viewModel.getActualUserUid() == viewModel.getActualUserUid()) {
            //region Menu opciones
            AnimatedVisibility(
                visible = viewModel.enableSettingsMenu, enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(
                        durationMillis = 300, easing = LinearOutSlowInEasing
                    )
                ), exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(
                        durationMillis = 200, easing = FastOutLinearInEasing
                    )
                )
            ) {
                //Si estoy en esta pantalla cuando le doy al botón de atrás no quiero cambiar de pantalla
                //Si no que este menú se vuelva a plegar
                BackPressHandler(viewModel::turnSidebarMenu)
                SideBar(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    switchSettings = viewModel::turnSidebarMenu
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        //TODO implementar
                        //region Botón para cambiar de tema
                        /*
                    Row() {
                        Box() {
                            TextIconButton(
                                text = viewModel.theme,
                                iconId = when (viewModel.theme) {
                                    stringResource(id = R.string.dark_theme) -> R.drawable.dark_mode
                                    stringResource(id = R.string.auto_theme) -> R.drawable.auto_mode
                                    stringResource(id = R.string.light_theme) -> R.drawable.light_mode
                                    else -> {
                                        R.drawable.error
                                    }
                                },
                                contentDescription = stringResource(id = R.string.theme)
                            ) {
                                viewModel.enableThemeMenu = true
                            }
                        }

                        DropdownMenu(
                            expanded = viewModel.enableThemeMenu,
                            onDismissRequest = { viewModel.enableThemeMenu = false }) {

                            DropdownThemeItem(
                                text = stringResource(id = R.string.dark_theme),
                                iconId = R.drawable.dark_mode
                            ) {
                                viewModel.setThemeToDark()
                                viewModel.enableThemeMenu = false
                            }

                            DropdownThemeItem(
                                text = stringResource(id = R.string.light_theme),
                                iconId = R.drawable.light_mode
                            ) {
                                viewModel.setThemeToLight()
                                viewModel.enableThemeMenu = false
                            }

                            DropdownThemeItem(
                                text = stringResource(id = R.string.auto_theme),
                                iconId = R.drawable.auto_mode
                            ) {
                                viewModel.setThemeToAuto()
                                viewModel.enableThemeMenu = false

                            }

                        }
                    }
                    */
                        //endregion

                        TextIconButton(
                            text = stringResource(id = R.string.logout),
                            iconId = R.drawable.logout,
                            contentDescription = stringResource(id = R.string.logout)
                        ) {
                            viewModel.logOut()
                            navController.navigate(Destinations.RegisterScreen.ruta) {
                                popUpTo(Destinations.HomeScreen.ruta) { inclusive = true }
                            }

                        }
                    }

                }
            }     //endregion
        }

    }

}

@Composable
fun ProfileScreenBody(
    navController: NavHostController? = null,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {




    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Row(Modifier.fillMaxHeight(0.25f)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.onSecondary)
                ) {

                    Box {
                            Image(
                                painter = rememberAsyncImagePainter(viewModel.banner),
                                contentDescription = "Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(175.dp)
                                    .background(MaterialTheme.colors.onSecondary)
                                    .clickable(enabled = viewModel.onEditMode) {
                                        
                                    }
                            )

                        Divider(
                            color = MaterialTheme.colors.primary,
                            thickness = 1.dp,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }

        //region BODY
        Column(

            Modifier
                .fillMaxSize()

                .offset(y = (-80).dp)
        ) {

            //region PFP
            Row(
                Modifier
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 25.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.background)
                    )

                    Image(
                        painter = rememberAsyncImagePainter(viewModel.pfp),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape)
                            .clickable(enabled = viewModel.onEditMode) {
                            }
                    )

                }


                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .offset(y = (85).dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.3f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                //todo
                            }, Arrangement.Bottom, Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "0",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                        Text(text = "Seguidores", style = MaterialTheme.typography.body1)
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                //todo
                            },
                        Arrangement.Bottom,
                        Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                        Text(text = "Siguiendo", style = MaterialTheme.typography.body1)
                    }
                }
            }

            Row(Modifier.padding(horizontal = 15.dp)) {
                Column {
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = viewModel.userData.name ?: "",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = viewModel.userData.description ?: "",
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                }

            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column() {
                    if (!viewModel.onEditMode) {
                        if (viewModel.getActualUserUid() == viewModel.getActualUserUid()) {
                            OutlinedButton(onClick = { viewModel.switchEditMode() }) {
                                Text(text = stringResource(id = R.string.edit_profile))
                            }
                        } else {
                            OutlinedButton(onClick = { /*todo*/ }) {
                                Text(text = stringResource(id = R.string.follow))
                            }
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            OutlinedButton(onClick = { /*todo*/ }) {
                                Text(text = stringResource(id = R.string.save_changes))
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            OutlinedButton(onClick = { viewModel.switchEditMode() }) {
                                Text(text = stringResource(id = R.string.cancel_changes))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }

            Row(Modifier.fillMaxWidth()) {

                Divider(
                    color = MaterialTheme.colors.onSecondary,
                    thickness = 1.dp,
                )
            }
        }


        //endregion


        //endregion


    }


}



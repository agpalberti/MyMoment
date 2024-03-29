package com.agp.mymoment.ui.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import com.agp.mymoment.config.MyPreferences
import com.agp.mymoment.navigation.Destinations
import com.agp.mymoment.ui.composables.*
import com.agp.mymoment.ui.image.ImageView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    userUID: String
) {

    //todo actualizar borrado de imagenes en tiempo real
    viewModel.getUserData(userUID)
    val context = LocalContext.current

    when(MyPreferences(context).accessTheme.collectAsState(initial = null).value){
        "true" ->  viewModel.setThemeToDark()
        "false" -> viewModel.setThemeToLight()
        else -> viewModel.setThemeToAuto()
    }

    var blur = 0.dp
    if (viewModel.openImageView) {
        BackPressHandler(onBackPressed = {
            viewModel.resetImageView()
        })
        blur = 10.dp
    }
    Box(modifier = Modifier.fillMaxSize().blur(blur, blur)) {

        //region navBar
        ThemedNavBar(navController = navController, topBarContent = {
            Row(Modifier.fillMaxWidth(0.5f), Arrangement.Start) {
                Text(
                    viewModel.userData.nickname ?: "",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary
                )
            }
            Row(Modifier.fillMaxWidth(), Arrangement.End) {
                if (userUID == viewModel.getLoggedUserUid()) {
                    NoRippleIconButton(
                        painterResource(id = R.drawable.menu_open),
                        contentDescription = stringResource(id = R.string.open_menu),
                        modifier = Modifier.size(30.dp)
                    ) {
                        //onClick
                        viewModel.turnDrawerMenu()
                    }
                }
            }
        })
        //endregion navBar
        {

            val list = viewModel.userData.posts?.sortedBy { it.date }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(0.dp)
            ) {
                item(span = { GridItemSpan(3) }) {
                    ProfileScreenBody(navController, userUID = userUID)
                }



                items(list?.size ?: 0) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-80).dp),
                        Arrangement.Center
                    ) {
                        ImageContainer(
                            image = list!![item].download_link ?: "",
                            contentDescription = "Image $item",
                            modifier = Modifier
                                .size(130.dp)
                                .padding(2.dp)
                        ){
                            viewModel.openImageView = true
                            viewModel.item = item
                        }


                    }
                }
            }

            if (viewModel.openImageView) {
                ImageView(
                    url = list?.get(viewModel.item)?.download_link ?: "",
                    pfpUrl = viewModel.pfp,
                    userUid = userUID,
                    onDeleteRequest = {viewModel.getUserData(userUID)},
                    onUserClick = {
                        navController.navigate("${Destinations.ProfileScreen.ruta}/${userUID}")
                        viewModel.resetImageView()
                    }) {
                    viewModel.resetImageView()
                }
            }

        }

        if (userUID == viewModel.getLoggedUserUid()) {
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
                BackPressHandler(viewModel::turnDrawerMenu)
                ModalDrawer(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    switchSettings = viewModel::turnDrawerMenu
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp), verticalArrangement = Arrangement.SpaceEvenly
                    ) {

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
                                    viewModel.setTheme(true,context)
                                    viewModel.enableThemeMenu = false

                                }

                                DropdownThemeItem(
                                    text = stringResource(id = R.string.light_theme),
                                    iconId = R.drawable.light_mode
                                ) {
                                    viewModel.setTheme(false,context)
                                    viewModel.enableThemeMenu = false
                                }

                                DropdownThemeItem(
                                    text = stringResource(id = R.string.auto_theme),
                                    iconId = R.drawable.auto_mode
                                ) {
                                    viewModel.setTheme(null,context)
                                    viewModel.enableThemeMenu = false

                                }

                            }
                        }

                        //endregion

                        TextIconButton(
                            text = stringResource(id = R.string.logout),
                            iconId = R.drawable.logout,
                            contentDescription = stringResource(id = R.string.logout)
                        ) {
                            navController.navigate(Destinations.RegisterScreen.ruta) {
                                popUpTo(Destinations.HomeScreen.ruta) { inclusive = true }
                                viewModel.logOut()
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
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    userUID: String
) {

    val context = LocalContext.current
    var bannerImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val bannerBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val bannerLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        bannerImageUri = uri
    }

    var pfpImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val pfpBitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val pfpLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pfpImageUri = uri
    }

    var seguidos = viewModel.userData.follows?.size?: 0
    var seguidores =viewModel.userData.followers?.size?: 0

    Log.i("wawa", viewModel.userData.follows?.size.toString())

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

                    //region Banner
                    Box {
                        //todo arreglar que el banner se vea bien en todos los dispositivos y rellene todo el tamaño
                        if (bannerImageUri == null) {
                            Image(
                                painter = rememberAsyncImagePainter(viewModel.banner),
                                contentDescription = stringResource(id = R.string.banner),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(175.dp)
                                    .background(MaterialTheme.colors.onSecondary)
                                    .clickable(enabled = viewModel.onEditMode) {
                                        bannerLauncher.launch("image/*")
                                    }
                            )
                        } else {
                            bannerImageUri?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    bannerBitmap.value = MediaStore.Images
                                        .Media.getBitmap(context.contentResolver, it)
                                } else {
                                    val source = ImageDecoder
                                        .createSource(context.contentResolver, it)
                                    bannerBitmap.value = ImageDecoder.decodeBitmap(source)
                                }
                                bannerBitmap.value?.let { btm ->
                                    Image(bitmap = btm.asImageBitmap(),
                                        contentDescription = stringResource(id = R.string.banner),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .height(175.dp)
                                            .background(MaterialTheme.colors.onSecondary)
                                            .clickable(enabled = viewModel.onEditMode) {
                                                bannerLauncher.launch("image/*")
                                            })
                                }
                            }

                        }
                        Divider(
                            color = MaterialTheme.colors.primary,
                            thickness = 1.dp,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                    //endregion
                }
            }
        }


        Column(
            Modifier
                .fillMaxSize()
                .offset(y = (-80).dp)
        ) {


            Row(
                Modifier
                    .height(IntrinsicSize.Max)
                    .padding(horizontal = 25.dp)
            ) {

                //region PFP
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

                    if (pfpImageUri == null) {
                        Image(
                            painter = rememberAsyncImagePainter(viewModel.pfp),
                            contentDescription = stringResource(id = R.string.pfp),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape)
                                .clickable(enabled = viewModel.onEditMode) {
                                    pfpLauncher.launch("image/*")
                                }
                        )
                    } else {
                        pfpImageUri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                pfpBitmap.value = MediaStore.Images
                                    .Media.getBitmap(context.contentResolver, it)
                            } else {
                                val source = ImageDecoder
                                    .createSource(context.contentResolver, it)
                                pfpBitmap.value = ImageDecoder.decodeBitmap(source)
                            }
                            pfpBitmap.value?.let { btm ->
                                Image(
                                    bitmap = btm.asImageBitmap(),
                                    contentDescription = stringResource(id = R.string.pfp),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(CircleShape)
                                        .clickable(enabled = viewModel.onEditMode) {
                                            pfpLauncher.launch("image/*")
                                        })
                            }
                        }
                    }

                }
                //endregion

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .offset(y = (85).dp)
                ) {

                    //todo update en tiempo real
                    //region Seguidores
                    Column(
                        Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight(0.3f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {

                                navController!!.navigate("${Destinations.FollowersScreen.ruta}/0/$userUID")
                            }, Arrangement.Bottom, Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "$seguidores",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = stringResource(id = R.string.followers),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    //endregion

                    //region Seguidos
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController!!.navigate("${Destinations.FollowersScreen.ruta}/1/$userUID")
                            },
                        Arrangement.Bottom,
                        Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$seguidos",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.primary
                        )
                        Text(
                            text = stringResource(id = R.string.following),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    //endregion
                }
            }

            //region Nombre y biografía
            Row(Modifier.padding(horizontal = 15.dp)) {
                if (!viewModel.onEditMode) {
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
                } else {
                    Column {
                        Spacer(modifier = Modifier.size(10.dp))

                        OutlinedTextField(
                            singleLine = true,
                            value = viewModel.editingName,
                            onValueChange = { viewModel.editingName = it })
                        Spacer(modifier = Modifier.size(10.dp))

                        OutlinedTextField(
                            value = viewModel.editingBio,
                            onValueChange = { viewModel.editingBio = it })
                        Spacer(modifier = Modifier.size(10.dp))
                    }

                }
                //endregion
            }
            //endregion

            //region Editar perfil
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column() {
                    if (!viewModel.onEditMode) {
                        if (userUID == viewModel.getLoggedUserUid()) {
                            OutlinedButton(onClick = { viewModel.switchEditMode() }) {
                                Text(text = stringResource(id = R.string.edit_profile))
                            }
                        } else {
                            if (!viewModel.isUserFollowing) {
                                OutlinedButton(onClick = {
                                    viewModel.follow(userUID)
                                    seguidores++
                                }) {
                                    Text(text = stringResource(id = R.string.follow))
                                }
                            } else {
                                OutlinedButton(onClick = {
                                    viewModel.unfollow(userUID)
                                    seguidores--
                                }) {
                                    Text(text = stringResource(id = R.string.unfollow))
                                }
                            }
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            OutlinedButton(onClick = {
                                if (pfpImageUri != null) viewModel.uploadNewPfp(
                                    pfpBitmap.value!!,
                                    context
                                )
                                if (bannerImageUri != null) viewModel.uploadNewBanner(
                                    bannerBitmap.value!!,
                                    context
                                )
                                viewModel.uploadUserData()
                                viewModel.getUserData(viewModel.getLoggedUserUid())
                                viewModel.switchEditMode()
                            }) {
                                Text(text = stringResource(id = R.string.save_changes))
                            }
                            Spacer(modifier = Modifier.size(10.dp))
                            OutlinedButton(onClick = {
                                bannerImageUri = null
                                pfpImageUri = null
                                viewModel.resetEditFields()
                                viewModel.switchEditMode()
                            }) {
                                Text(text = stringResource(id = R.string.cancel_changes))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
            //endregion

            Row(Modifier.fillMaxWidth()) {

                Divider(
                    color = MaterialTheme.colors.onSecondary,
                    thickness = 1.dp,
                )
            }

        }

    }

}
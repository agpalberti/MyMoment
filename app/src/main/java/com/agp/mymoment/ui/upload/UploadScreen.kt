package com.agp.mymoment.ui.upload

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.agp.mymoment.R
import com.agp.mymoment.ui.composables.BackPressHandler
import com.agp.mymoment.ui.composables.CameraCapture
import com.agp.mymoment.ui.composables.GallerySelect
import com.agp.mymoment.ui.composables.ThemedNavBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun UploadScreen(navController: NavHostController) {
    ThemedNavBar(navController = navController, topBarContent = {}) {
        UploadScreenBody(navController)
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun UploadScreenBody(
    navController: NavHostController? = null,
    viewModel: UploadScreenViewModel = hiltViewModel()
) {

    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val context = LocalContext.current

    if (viewModel.imageUri != viewModel.emptyImageUri) {
        Box(modifier = Modifier) {
            BackPressHandler(onBackPressed = { viewModel.imageUri = viewModel.emptyImageUri })
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(viewModel.imageUri),
                contentDescription = "Captured image"
            )
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = {
                    viewModel.imageUri = viewModel.emptyImageUri
                }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                    contentDescription = "Ir atrás",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    //TODO
                    bitmap.value?.let { viewModel.uploadNewPost(it,context) }
                    viewModel.imageUri = viewModel.emptyImageUri
                    bitmap.value = null

                }) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.next),
                    contentDescription = "Subir",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }
        }
    } else {

        if (!viewModel.showGallerySelect) {
            CameraCapture(
                modifier = Modifier,
                onImageFile = { file ->
                    if (file != null) {
                        viewModel.imageUri = file.toUri()
                        viewModel.imageUri?.let {
                            if (Build.VERSION.SDK_INT < 28) {
                                bitmap.value = MediaStore.Images
                                    .Media.getBitmap(context.contentResolver, it)
                            } else {
                                val source = ImageDecoder
                                    .createSource(context.contentResolver, it)
                                bitmap.value = ImageDecoder.decodeBitmap(source)
                            }
                        }
                    } else viewModel.showGallerySelect = true
                }
            )
        } else {
            GallerySelect(
                modifier = Modifier,
                onImageUri = { uri ->
                    viewModel.showGallerySelect = false
                    viewModel.imageUri = uri
                    viewModel.imageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value = MediaStore.Images
                                .Media.getBitmap(context.contentResolver, it)
                        } else {
                            val source = ImageDecoder
                                .createSource(context.contentResolver, it)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }
                    }
                }
            )
        }
    }
}


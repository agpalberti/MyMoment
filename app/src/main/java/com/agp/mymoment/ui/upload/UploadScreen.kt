package com.agp.mymoment.ui.upload

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
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
                    viewModel.imageUri = viewModel.emptyImageUri
                }) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.next),
                    contentDescription = "Subir",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            }
        }
    } else {
            CameraCapture(
                modifier = Modifier,
                onImageFile = { file ->
                    viewModel.imageUri = file.toUri()
                }
            )
    }
}


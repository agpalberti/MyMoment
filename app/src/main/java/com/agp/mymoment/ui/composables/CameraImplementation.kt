package com.agp.mymoment.ui.composables

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.agp.mymoment.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Permission(
    permission: String = Manifest.permission.CAMERA,
    rationale: String = "This permission is important for this app. Please grant the permission.",
    permissionNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                text = rationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        permissionNotAvailableContent = permissionNotAvailableContent,
        content = content
    )
}

@Composable
fun Rationale(
    text: String,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = stringResource(id = R.string.camera))
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text(stringResource(id = R.string.ok))
            }
        }
    )
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onUseCase: (UseCase) -> Unit = { }
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            onUseCase(Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            )
            previewView
        }
    )
}


@ExperimentalPermissionsApi
@ExperimentalCoroutinesApi
@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalZeroShutterLag::class)
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File?) -> Unit = { }
) {

    var camera by remember {
        mutableStateOf(cameraSelector)
    }
    var onClickEnable by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    var frontCamera = false
    Permission(
        permission = Manifest.permission.CAMERA,
        rationale = stringResource(id = R.string.camera_permission),
        permissionNotAvailableContent = {
            Column(modifier) {
                Text(stringResource(id = R.string.no_camera))
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }) {
                    Text(stringResource(id = R.string.settings))
                }
            }
        }
    ) {
        Box(modifier = modifier) {

            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_ZERO_SHUTTER_LAG)
                        .build()
                )
            }
            Box {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    }
                )

                Row(
                    Modifier
                        .align(Alignment.BottomStart)
                        .offset(y = (-50).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(enabled = onClickEnable, modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                        onClick = {
                            coroutineScope.launch {
                                onClickEnable = false

                                onImageFile(null)

                                delay(1000)
                                onClickEnable = true
                            }
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gallery),
                            contentDescription = stringResource(id = R.string.gallery)
                        )
                    }
                }

                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-50).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        enabled = onClickEnable,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp),
                        onClick = {

                            coroutineScope.launch {
                                onClickEnable = false
                                imageCaptureUseCase.takePicture(context.executor).let {
                                    onImageFile(it)
                                    delay(1000)
                                    onClickEnable = true
                                }
                            }
                        }
                    ) {
                        Image(
                            modifier = Modifier.size(120.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.camera),
                            contentDescription = stringResource(id = R.string.camera)
                        )
                    }
                }

                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .offset(y = (-50).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(enabled = onClickEnable, modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp),
                        onClick = {
                            coroutineScope.launch {
                                onClickEnable = false
                                camera =
                                    if (camera == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        frontCamera = !frontCamera
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        frontCamera = !frontCamera
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                                delay(1000)
                                onClickEnable = true
                            }
                        }) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.cameraswitch),
                            contentDescription = stringResource(id = R.string.swich_camera)
                        )
                    }
                }
            }
            LaunchedEffect(frontCamera) {
                // FIXME: Flip foto camara frontal
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, camera, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("Camara", "Failed to bind camera use cases", ex)
                }
            }
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { c ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener({
            c.resume(future.get())
        }, executor)
    }
}


@Composable
fun GallerySelect(
    modifier: Modifier = Modifier,
    onImageUri: (Uri) -> Unit = { }
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI)
        }
    )

    @Composable
    fun LaunchGallery() {
        SideEffect {
            launcher.launch("image/*")
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Permission(
            permission = Manifest.permission.ACCESS_MEDIA_LOCATION,
            rationale = stringResource(id = R.string.gallery_permission),
            permissionNotAvailableContent = {
                Column(modifier) {
                    Text(stringResource(id = R.string.no_gallery))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                })
                            }
                        ) {
                            Text(stringResource(id = R.string.settings))
                        }
                        // If they don't want to grant permissions, this button will result in going back
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                onImageUri(EMPTY_IMAGE_URI)
                            }
                        ) {
                            Text(stringResource(id = R.string.use_camera))
                        }
                    }
                }
            },
        ) {
            LaunchGallery()
        }
    } else {
        LaunchGallery()
    }
}

suspend fun ImageCapture.takePicture(executor: Executor): File {
    val photoFile = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            File.createTempFile("image", "jpg")
        }.getOrElse { ex ->
            Log.e("Camara", "Failed to create temporary file", ex)
            File("/dev/null")
        }
    }

    return suspendCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                continuation.resume(photoFile)
            }

            override fun onError(e: ImageCaptureException) {
                Log.e("Camara", "Image capture failed", e)
                continuation.resumeWithException(e)
            }
        })
    }
}
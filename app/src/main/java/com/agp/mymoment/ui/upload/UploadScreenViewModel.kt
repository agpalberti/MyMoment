package com.agp.mymoment.ui.upload

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class UploadScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){
    val emptyImageUri = Uri.parse("file://dev/null")
    var imageUri by savedStateHandle.saveable { mutableStateOf(emptyImageUri) }
}
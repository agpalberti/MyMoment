package com.agp.mymoment.ui.upload

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.model.DBM
import com.agp.mymoment.model.utilities.bitmapToPNG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class UploadScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){
    val emptyImageUri = Uri.parse("file://dev/null")
    var imageUri by savedStateHandle.saveable { mutableStateOf(emptyImageUri) }
    var showGallerySelect by savedStateHandle.saveable { mutableStateOf(false) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadNewPost(bitmap: Bitmap, context: Context){
        val post = bitmapToPNG(bitmap, context)
        if (post != null) {
            viewModelScope.launch { DBM.uploadNewPost(post) }
        } else Log.e("Post", "Se ha intentado subir un post null")
    }
}
package com.agp.mymoment.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.R
import com.agp.mymoment.config.MyPreferences
import com.agp.mymoment.model.DBM
import com.agp.mymoment.model.classes.Post
import com.agp.mymoment.model.classes.User
import com.agp.mymoment.model.utilities.bitmapToPNG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ProfileScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var pfp by savedStateHandle.saveable { mutableStateOf("") }
    var banner by savedStateHandle.saveable { mutableStateOf("") }
    var enableSettingsMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var enableThemeMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var theme by savedStateHandle.saveable { mutableStateOf(MyPreferences.resources!!.getString(R.string.auto_theme)) }
    var userData by savedStateHandle.saveable { mutableStateOf(User()) }
    var onEditMode by savedStateHandle.saveable{ mutableStateOf(false) }
    var bitmap by savedStateHandle.saveable { mutableStateOf<Bitmap>(Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)) }
    var editingName by savedStateHandle.saveable { mutableStateOf("") }
    var editingBio by savedStateHandle.saveable { mutableStateOf("") }

    fun turnSidebarMenu() {
        enableSettingsMenu = !enableSettingsMenu
    }

    fun switchEditMode(){
        onEditMode = ! onEditMode
    }

    fun logOut() {
        DBM.onLogOut()
    }

    fun setThemeToLight() {
        theme = MyPreferences.resources!!.getString(R.string.light_theme)
    }

    fun setThemeToAuto() {
        theme = MyPreferences.resources!!.getString(R.string.auto_theme)

    }

    fun setThemeToDark() {
        theme = MyPreferences.resources!!.getString(R.string.dark_theme)
    }

    fun updateUserData(uid:String){
        viewModelScope.launch {
            DBM.getUserData(uid).collect {
                userData = it
                updateImages(uid)
            }

        }
        resetEditFields()
    }

    fun uploadUserData(){
        DBM.uploadUserData(editingName, userData.nickname!!, editingBio)
    }
    fun resetEditFields(){
        editingName = userData.name?:""
        editingBio = userData.description?:""
    }
    fun getActualUserUid() = DBM.getLoggedUserUid()

    private suspend fun updateImages(uid: String) {
        this.pfp = DBM.getPFP(uid)
        this.banner = DBM.getBanner(uid)

    }

    fun uploadNewPfp(bitmap: Bitmap, context: Context){
        val pfp = bitmapToPNG(bitmap, context)
        if (pfp != null) {
            DBM.uploadNewPfp(pfp)
        }
    }

    fun uploadNewBanner(bitmap: Bitmap, context: Context){
        val banner = bitmapToPNG(bitmap, context)
        if (banner != null) {
            DBM.uploadNewBanner(banner)
        }
    }
}


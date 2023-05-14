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
import com.agp.mymoment.config.MyResources
import com.agp.mymoment.model.DBM
import com.agp.mymoment.model.classes.User
import com.agp.mymoment.model.utilities.bitmapToPNG
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ProfileScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var item: Int = 0
    var openImageView: Boolean by savedStateHandle.saveable { mutableStateOf(false) }
    var pfp by savedStateHandle.saveable { mutableStateOf("") }
    var banner by savedStateHandle.saveable { mutableStateOf("") }
    var enableSettingsMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var enableThemeMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var theme by savedStateHandle.saveable { mutableStateOf(MyResources.resources!!.getString(R.string.auto_theme)) }
    var userData by savedStateHandle.saveable { mutableStateOf(User()) }
    var onEditMode by savedStateHandle.saveable { mutableStateOf(false) }
    var editingName by savedStateHandle.saveable { mutableStateOf("") }
    var editingBio by savedStateHandle.saveable { mutableStateOf("") }
    var isUserFollowing by savedStateHandle.saveable {
        mutableStateOf(false)
    }


    fun resetImageView(){
            openImageView = false
    }

    fun turnSidebarMenu() {
        enableSettingsMenu = !enableSettingsMenu
    }

    fun switchEditMode() {
        onEditMode = !onEditMode
    }

    fun logOut() {
        DBM.onLogOut()
    }

    fun setTheme(darkTheme: Boolean?, context: Context) {
        val myPreferences = MyPreferences(context)
        viewModelScope.launch {
            myPreferences.saveThemeSetting(darkTheme)
        }

        when (darkTheme) {
            true -> setThemeToDark()
            false -> setThemeToLight()
            null -> setThemeToAuto()
        }
    }

    fun setThemeToLight() {
        theme = MyResources.resources!!.getString(R.string.light_theme)
    }

    fun setThemeToAuto() {
        theme = MyResources.resources!!.getString(R.string.auto_theme)

    }

    fun setThemeToDark() {
        theme = MyResources.resources!!.getString(R.string.dark_theme)
    }

    fun getUserData(uid: String) {
        viewModelScope.launch {
            DBM.getUserData(uid).collect {
                userData = it
                updateImages(uid)
                updateIsFollowing(uid)
                Log.i("ProfileScreenViewModel", "getUserData: ${userData.followers}")
            }

        }
        resetEditFields()
    }

    fun uploadUserData() {
        DBM.uploadUserData(
            editingName,
            userData.nickname!!,
            editingBio,
            userData.posts ?: emptyList(),
            userData.follows ?: emptyList(),
            userData.followers ?: emptyList()
        )
    }

    private suspend fun updateIsFollowing(uid: String) {
        this.isUserFollowing = DBM.isFollowing(DBM.getLoggedUserUid(), uid)
    }

    fun follow(uid: String) {
        if (!isUserFollowing) {
            viewModelScope.launch {
                DBM.followUser(uid)
                isUserFollowing = true
            }
        }
    }

    fun unfollow(uid: String) {
        if (isUserFollowing) {
            viewModelScope.launch {
                DBM.unfollowUser(uid)
                isUserFollowing = false
            }
        }
    }

    fun resetEditFields() {
        editingName = userData.name ?: ""
        editingBio = userData.description ?: ""
    }

    fun getCurrentUserUid() = DBM.getLoggedUserUid()

    private suspend fun updateImages(uid: String) {
        this.pfp = DBM.getPFP(uid)
        this.banner = DBM.getBanner(uid)

    }

    fun uploadNewPfp(bitmap: Bitmap, context: Context) {
        val pfp = bitmapToPNG(bitmap, context)
        if (pfp != null) {
            DBM.uploadNewPfp(pfp)
        }
    }

    fun uploadNewBanner(bitmap: Bitmap, context: Context) {
        val banner = bitmapToPNG(bitmap, context)
        if (banner != null) {
            DBM.uploadNewBanner(banner)
        }
    }

}


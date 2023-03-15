package com.agp.mymoment.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.R
import com.agp.mymoment.config.MyPreferences
import com.agp.mymoment.model.DBM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class ProfileScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel(){

    var enableSettingsMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var enableThemeMenu by savedStateHandle.saveable { mutableStateOf(false) }
    var theme by savedStateHandle.saveable{ mutableStateOf( MyPreferences.resources!!.getString(R.string.auto_theme)) }


    fun turnSidebarMenu(){
        enableSettingsMenu = !enableSettingsMenu
    }

    fun logOut() {
        DBM.onLogOut()
    }

    fun setThemeToLight(){
        theme = MyPreferences.resources!!.getString(R.string.light_theme)
    }

    fun setThemeToAuto(){
        theme = MyPreferences.resources!!.getString(R.string.auto_theme)

    }

    fun setThemeToDark(){
        theme = MyPreferences.resources!!.getString(R.string.dark_theme)
    }



}
package com.agp.mymoment.ui.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel(){

    var correo by savedStateHandle.saveable { mutableStateOf("") }

    var user by savedStateHandle.saveable { mutableStateOf("") }

    var nombre by savedStateHandle.saveable { mutableStateOf("") }

    var password by savedStateHandle.saveable { mutableStateOf("") }

    var passwordVisible by savedStateHandle.saveable { mutableStateOf(false) }

    var register by savedStateHandle.saveable {
        mutableStateOf(true)
    }



}
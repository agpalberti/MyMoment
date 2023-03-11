package com.agp.mymoment.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.model.DBM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel(){


    var email by savedStateHandle.saveable { mutableStateOf("") }

    var nickname by savedStateHandle.saveable { mutableStateOf("") }

    var name by savedStateHandle.saveable { mutableStateOf("") }

    var password by savedStateHandle.saveable { mutableStateOf("") }

    var passwordVisible by savedStateHandle.saveable { mutableStateOf(false) }

    var isOnRegister by savedStateHandle.saveable {
        mutableStateOf(true)
    }


    fun register() {
        DBM.onRegister(email,password,nickname,name)
    }

    fun login() {
        DBM.onLogin(email,password)
    }


}
package com.agp.mymoment.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.agp.mymoment.R
import com.agp.mymoment.config.MyResources
import com.agp.mymoment.model.DBM
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class RegisterScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    ViewModel() {


    var email by savedStateHandle.saveable { mutableStateOf("") }

    var emailError by savedStateHandle.saveable() { mutableStateOf("") }

    var nickname by savedStateHandle.saveable { mutableStateOf("") }

    var nicknameError by savedStateHandle.saveable { mutableStateOf("") }

    var name by savedStateHandle.saveable { mutableStateOf("") }

    var nameError by savedStateHandle.saveable { mutableStateOf("") }

    var password by savedStateHandle.saveable { mutableStateOf("") }

    var passwordError by savedStateHandle.saveable { mutableStateOf("") }

    var passwordVisible by savedStateHandle.saveable { mutableStateOf(false) }

    var isOnRegister by savedStateHandle.saveable {
        mutableStateOf(true)
    }

    fun register(callback: (Boolean) -> Unit) {
        DBM.onRegister(
            email.trim().lowercase(Locale.ROOT),
            password,
            nickname.trim(),
            name.trim()
        ) { errorCode ->
            if (errorCode == 0) callback(true)
            else {
                parseErrorCode(errorCode)
                callback(false)
            }
        }
    }

    fun login(callback: (Boolean) -> Unit) {
        DBM.onLogin(email.trim().lowercase(Locale.ROOT), password) { errorCode ->
            if (errorCode == 0) callback(true)
            else {
                parseErrorCode(errorCode)
                callback(false)
            }
        }
    }

    fun getErrorString():String{
        listOf(emailError,nicknameError,nameError,passwordError).forEach{
            if (it.isNotEmpty()) return it
        }
        return ""
    }

   fun resetErrors() {
        emailError = ""
        nicknameError = ""
        nameError = ""
        passwordError = ""
    }

    private fun parseErrorCode(errorCode: Int?) {
        when (errorCode) {
            1 -> {
                emailError = MyResources.resources?.getString(R.string.emailInUseError)!!
            }//Email en uso
            2 -> {
                passwordError = MyResources.resources?.getString(R.string.passwordTooShortError)!!
            }//La contraseña debe contener 6 carácteres de largo como mínimo
            3 -> {
                emailError = MyResources.resources?.getString(R.string.emptyEmailError)!!
            }//Email vacío
            4 -> {
                passwordError = MyResources.resources?.getString(R.string.emptyPasswordError)!!
            }//Contraseña vacía
            5 -> {
                nameError = MyResources.resources?.getString(R.string.emptyNameError)!!
            }//El nombre no puede estar vacío
            6 -> {
                nicknameError = MyResources.resources?.getString(R.string.emptyNicknameError)!!
            }//El nombre de usuario no puede estar vacío
            7 -> {
                passwordError = MyResources.resources?.getString(R.string.invalidPasswordError)!!
            }//Contraseña incorrecta
            8 -> {
                emailError = MyResources.resources?.getString(R.string.invalidUserError)!!
            }//Usuario inexistente
            9 -> {
                emailError = MyResources.resources?.getString(R.string.invalidEmailFormatError)!!
            }
            else -> {}
        }
    }

}